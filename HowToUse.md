# How to use jhocr #

Create your searchable pdf from an image using the tesseract java api tess4j or the tesseract binaries installed on your system.

## Embededd ##

Create your searchable pdf from an image using the tesseract java api tess4j.

tbd ...

---

## On MacOSX ##

### MacOSX Part 1 ###

Get home brew installed.

  1. check the [home brew page](http://brew.sh/index_de.html) for newer howtos
  1. otherwise run on your terminal (don't run it as sudo):
```
ruby -e "$(curl -fsSL https://raw.github.com/mxcl/homebrew/go/install)"
```
  1. accept xcode install
  1. run on your terminal:
```
brew doctor
```
  1. follow the steps an than run:
```
export PATH='/usr/local/bin:$PATH' >> ~/.bash_profile
```

### MacOSX Part 2 ###

  1. run on your terminal:
```
brew install tesseract
```
  1. grab a bier
  1. wait untill finished and than test your installation with:
```
$ tesseract -v
tesseract 3.02.02
 leptonica-1.69
  libjpeg 8d : libpng 1.5.17 : libtiff 4.0.3 : zlib 1.2.5
```

Sources used for this article:
  * [Installing Tesseract on a Mac](http://emop.tamu.edu/node/45)
  * [Home Brew](http://brew.sh/index_de.html)

## On Windows ##

  1. Next, Next, Next and Finish :)

tbd ...

## On Linux ##

  1. sudo apt-get install tesseract-ocr

tbd ...