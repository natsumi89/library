package com.library.controller;

import com.library.domain.Book;
import com.library.domain.User;
import com.library.form.UserForm;
import com.library.service.BookService;
import com.library.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private HttpSession session;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register-user-information")
    public String insert(@Valid UserForm userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("userForm", userForm);
            model.addAttribute("bindingResult", bindingResult);
            return "registration";
        }

        User user = userService.findByEmail(userForm.getEmail());

        if (user != null) {
            model.addAttribute("error", "このメールアドレスはすでに登録されています");
            return "registration";
        }

        User newUser = new User();
        newUser.setLastName(userForm.getLastName());
        newUser.setFirstName(userForm.getFirstName());
        newUser.setBirthDate(userForm.getBirthDate());
        newUser.setEmail(userForm.getEmail());
        newUser.setPassword(userForm.getPassword());

        String encodedPassword = passwordEncoder.encode(userForm.getPassword());
        newUser.setPassword(encodedPassword);

        userService.insert(newUser);
        return "redirect:/top";
    }

    @PostMapping("/login-to-my-page")
    public String loginSuccessRedirect(UserForm userForm, Model model, HttpServletRequest request) {
        // 入力されたメールアドレスとパスワードを取得
        String email = userForm.getEmail();
        String password = userForm.getPassword();
        System.out.println("入力されたメールアドレス: " + email);
        System.out.println("入力されたパスワード: " + password);

        // ユーザーをデータベースから取得
        User authenticatedUser = userService.findByEmail(email);

        // メールアドレスが空でないことを確認
        if (email == null || email.isEmpty()) {
            model.addAttribute("errorMessage", "メールアドレスを入力してください");
            return "login";
        }

        // パスワードが空でないことを確認
        if (password == null || password.isEmpty()) {
            model.addAttribute("errorMessage", "パスワードを入力してください");
            return "login";
        }

        // ユーザーが取得されたかを確認
        if (authenticatedUser != null) {
            System.out.println("データベースから取得したユーザー情報: " + authenticatedUser.toString());
            // 入力されたパスワードをハッシュ化してから比較
            if (passwordEncoder.matches(password, authenticatedUser.getPassword())) {
                // パスワードが一致した場合の処理
                session.setAttribute("email", authenticatedUser.getEmail());
                session.setAttribute("lastName", authenticatedUser.getLastName());
                session.setAttribute("firstName", authenticatedUser.getFirstName());
                session.setAttribute("userId", authenticatedUser.getUserId());
                return "redirect:/to-my-page"; // ログイン成功時のリダイレクト先
            } else {
                // パスワードが一致しない場合の処理
                model.addAttribute("errorMessage", "メールアドレスまたはパスワードが間違っています");
                return "login";
            }
        } else {
            // ユーザーが取得されなかった場合の処理
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが間違っています");
            return "login";
        }
    }

    @GetMapping("/to-registration-page")
    public String toRegisterPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "registration";
    }

    @GetMapping("/login")
    public String toLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが間違っています");
        }
        model.addAttribute("userForm", new UserForm());
        return "login";
    }

    @GetMapping("/to-my-page")
    public String toMyPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        List<Book> loanedBooks = bookService.loanList(userId);
        model.addAttribute("loanedBooks", loanedBooks);
        model.addAttribute("userId", userId);

        return "my-page";
    }

    @PostMapping("/logout")
    public String logout() {
        Integer userId = (Integer) session.getAttribute("userId");
        session.invalidate();
        return "redirect:/back";
    }
}
