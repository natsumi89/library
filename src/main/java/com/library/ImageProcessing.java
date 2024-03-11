//package com.library;
//
//import py4j.GatewayServer;
//
//import java.io.File;
//
//public class ImageProcessing {
//
//    // Pythonスクリプトを呼び出すメソッド
//    public void callPythonScript() {
//        try {
//            // Py4JのGatewayServerを起動してPython側と接続する
//            GatewayServer gatewayServer = new GatewayServer(this);
//            gatewayServer.start();
//
//            // Python側のgenerate_imagesメソッドを呼び出す
//            generate_images("src/main/resources/static/img/generate", 10);
//
//            // GatewayServerを停止する
//            gatewayServer.shutdown(); // Python側の処理が完了した後に停止する必要がある場合はこちらをコメントインする
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Python側から呼び出されるメソッド
//    public void generate_images(String output_dir, int num_images) {
//        try {
//            // Pythonスクリプトのロジックを呼び出す
//            ProcessBuilder processBuilder = new ProcessBuilder("/Users/nn/.pyenv/shims/python", "generate_images.py");
//            processBuilder.directory(new File("/Users/nn/ワークスペース/library/src/main/python"));
//            processBuilder.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        ImageProcessing imageProcessing = new ImageProcessing();
//        imageProcessing.callPythonScript();
//    }
//}
