import fitz
import os

# Should be absolute path
filepath = input("Enter pdf file path: ")
prefix = input("Enter image prefix: ")
data_path = f"../data/images/{prefix}"
if (not os.path.exists(data_path)):
    os.mkdir(data_path)


pdf_file = fitz.open(filepath)
for idx in range(len(pdf_file)):
    page = pdf_file[idx]
    images = page.get_images()
    for img_index, img in enumerate(images):
        x_ref = img[0]
        base_image = pdf_file.extract_image(x_ref)
        image_data = base_image["image"]
        with open(f"{data_path}/page{idx + 1}_image{idx + 1}.png", "wb") as f:
            f.write(image_data)
