### How to run on Google Collab:

1. Upload the jupyter notebook onto google collab

2. Upload the `.mat` file into the BASE FOLDER of your google drive

3. Run the following code segment:

   ```python
   """
   Import Data
   
   Only do this for Google Collab
   
   Link followed: https://stackoverflow.com/questions/59622884/how-to-load-matlab-file-in-colab
   Upload the OQC mat file into the ROOT directory of your google drive. If not it will not work.
   """
   
   from google.colab import drive
   import scipy.io
   
   drive.mount('/content/drive')
   
   data=scipy.io.loadmat('/content/drive/MyDrive/OQC.mat');
   print("Header information: {info}".format(info = data['__header__']))
   data = data['data']
   print("Length of Data: {length} data points".format(length = str(len(data))))
   ```

4. DO NOT RUN (this code segment is only for running Jupyter Notebook locally):

   ```python
   """
   Import Data
   
   Do this when running Jupyter notebook locally
   """
   
   data = scipy.io.loadmat(os.getcwd() + '/OQC.mat')
   print("Header information: {info}".format(info = data['__header__']))
   data = data['data']
   print("Length of Data: {length} data points".format(length = str(len(data))))
   ```

   

