## Development Setup

CFE Tutorial: https://www.youtube.com/watch?v=iluST-V757A&list=PLEsfXFp6DpzRyxnU-vfs3vk-61Wpt7bOS&index=1


## Things learnt:

- basics of pixels and frames (media, video)
- basics of OpenCV (opening camera stream, taking photos/videos, color manipultation etc,)
- Using Haar Classifiers
- Creating your own models and using them in projects (face recognizer)
- Drawing rectangles and shapes on the video/image 
- Overlaying streams with watermarks
- Timelapsing
- Color overlay

## Notes

1. Other debugging things

    - https://stackoverflow.com/questions/12767669/import-error-no-module-named-appkit

2. Install OpenCV

    - https://www.codingforentrepreneurs.com/blog/install-opencv-3-for-python-on-mac/
    
    - https://www.geeksforgeeks.org/setup-opencv-with-pycharm-environment/
    
    - https://stackoverflow.com/questions/43818513/cant-show-the-video-on-python-opencv  
    
    - I don't know if you had to install opencv and all of that shit but I followed this link here and could get it started!

    - https://stackoverflow.com/questions/39802460/opencv-webcam-window-not-opening/39802509
    
3. Introduction to OpenCV

    - If you run program on pycharm, must allow pycharm permissions to camera

    - .waitKey() is basically a delay from waiting key to be pressed
    
    - you will need the below statement to launch a window with video stream
        ```
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        ```
    - set() function changes resolution (can't really see difference though LOL)
      
        1. https://www.programmersought.com/article/31213695010/
        
    - resize() function resizes the frame
      
        1. https://stackoverflow.com/questions/58887056/resize-frame-of-cv2-videocapture
        
    - frame != resolution take note!!!!!
    
4. Video Recording in OpenCV

    - OpenCV can't really deal with audio recording
    
    - If you really want to do audio recording with OpenCV, ffmpeg is the way to go        
    
    - VideoWriter() width and height must exactly match the width and height of the window, if not the output of the video will be at 6kb and 00:00 length (unplayable)
    
    - If you want to create .mp4 files, follow link here https://stackoverflow.com/questions/38397964/cant-write-and-save-a-video-file-using-opencv-and-python/55576859
    
    - More debugging dor .mp4 files https://stackoverflow.com/questions/52932157/opencv-ffmpeg-tag-0x34363268-h264-is-not-supported-with-codec/56723380
    
    - **For simplicity, just stick to .avi**
    
    - More on FourCC https://www.geeksforgeeks.org/saving-operated-video-from-a-webcam-using-opencv
    
5. How to improve accuracy

   - Images same size (consistency)
   
   - Find the optimal resolution (currently using 550 x 550)
   
   - Try to start from improving the DATA
   
   - Tinker around with different    
   
5. HEAD POSTURE 

    - https://learnopencv.com/head-pose-estimation-using-opencv-and-dlib/
    
    - https://towardsdatascience.com/real-time-head-pose-estimation-in-python-e52db1bc606a
    
    - https://medium.com/analytics-vidhya/real-time-head-pose-estimation-with-opencv-and-dlib-e8dc10d62078
    
    