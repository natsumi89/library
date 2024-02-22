# generate_images.py
from PIL import Image
from py4j.java_gateway import JavaGateway

def generate_images(output_dir, num_images, image_width=50, image_height=60, background_color=(255, 255, 255)):
    try:
        # Javaから渡されたディレクトリに画像を生成して保存
        for i in range(num_images):
            # 画像を生成
            image = Image.new("RGB", (image_width, image_height), background_color)

            # 画像を保存
            image.save(os.path.join(output_dir, f"image_{i}.png"))
    except Exception as e:
        e.printStackTrace()

# Java Gatewayの起動
gateway = JavaGateway()
# Java側から呼び出されるPythonメソッドの登録
gateway.entry_point.register(generate_images)
