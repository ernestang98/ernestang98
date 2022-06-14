import cv2
import ProcessingImage
import os


def get_latest_image(dirpath, valid_extensions=('jpg','jpeg','png')):
    """
    Get the latest image file in the given directory
    """
    # Get filepaths of all files and dirs in the given dir
    valid_files = [os.path.join(dirpath, filename) for filename in os.listdir(dirpath)]
    # Filter out directories, no-extension, and wrong extension files
    valid_files = [f for f in valid_files if '.' in f and \
        f.rsplit('.',1)[-1] in valid_extensions and os.path.isfile(f)]

    if not valid_files:
        raise ValueError("No valid images in %s" % dirpath)

    return max(valid_files, key=os.path.getmtime)

def main():
    # Get the latest image file and parse it into the variable image, from a given directory
    # Change the file path under dirpath
    image = get_latest_image(dirpath=r'C:\Users\gohti\OneDrive\Desktop\yolov5-master', valid_extensions=('jpg', 'jpeg', 'png'))
    # Read the image file as a numpy array
    picture = cv2.imread(image, cv2.IMREAD_UNCHANGED)


    #use this if you want to run rgbToGray
    #return preprocessingImg.rgbToGray(picture)

    #if not, go straight to png processing and detection
    return ProcessingImage.RunModel(picture)

if __name__ == "__main__":
    main()