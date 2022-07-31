import pandas as pd
import matplotlib
import seaborn as sb
import io
import os

if __name__ == '__main__':
    from PIL import Image

matplotlib.use('agg')


def regression_plot():
    abspath = os.path.abspath(__file__)
    dname = os.path.dirname(abspath)
    df = pd.read_csv(dname + '/data.csv')
    # df = pd.read_csv('data.csv')
    sb_plot = sb.regplot(x="rainfall", y="temperature", data=df)
    image = io.BytesIO()
    sb_plot.figure.savefig(image, format="png")
    image.seek(0)
    return image


if __name__ == "__main__":
    image = regression_plot()
    im = Image.open(image)
    im.save("regress.png", "PNG")
