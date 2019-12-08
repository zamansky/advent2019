import random
import sys
import getopt


def genpixel(pixel,numlayers):
    """
    generate a list with the real pixel in a random spot.
    Everything before it will be either transparent (2) or the real pixel colors
    Everything after will be random selected from all 3 values
    """
    realpixel =  random.randrange(numlayers)
    first_part = [random.choice([pixel,2]) for x in range(realpixel)]
    last_part = [random.randrange(0,3) for x in range(realpixel,numlayers-1)]
    layer = first_part + [pixel] + last_part
    return layer

def usage():
    s = "Usage: " + sys.argv[0]+ \
    """
    -h | -l number_of_layers\n
    5 layers is the default
    width, height, and number of layers will be printed to stderr
    the encoded image to stdout

    input: an ascii image
    """
    return s


# default number of layers
layers = 5

# get the command line opts.
try:
    opts, args = getopt.getopt(sys.argv[1:], "hl:")
except getopt.GetoptError as err:
    # print help information and exit:
    print( str(err))  # will print something like "option -a not recognized"
    print(usage())
    sys.exit(2)
output = None
verbose = False
for o, a in opts:
    if o == "-h":
        print(usage())
        sys.exit(2)
    elif o in ("-l"):
        layers = int(a)
    else:
        assert False, "unhandled option"


# read in the data
data = sys.stdin.read()

# convert to individual lines so we can figure out the width and height
image = ""
for c in data:
    if c != '\n' and c!= ' ':
        image = image + "*"
    else:
        image = image + c

lines = image.split("\n")
width = len(lines[0])
height = len(lines)


# Get rid of newlines so we can loop over the data set to make a single long list of all the pixels in teh image
# 0 for black 1 for white like in the problem
data = data.replace("\n","")
l = []
for d in data:
    if d==' ':
        pixel = 0 # black
    else:
        pixel = 1 # white
    l.append(genpixel(pixel, layers)) # genpixel makes a list of values one for each layer for each pixel



# loop once for each layer.
# each time, add the pixels for that layer to the encoded image which holds all the layers
encoded_image=""
for which_pixel in range(layers):
    for p in l:
        encoded_image = encoded_image + str(p[which_pixel])

# print the layers to stdout and the specs (height,width,layers) to stdout.

print("Width: "+str(width)+" Height: "+str(height)+" Layers: "+str(layers)+"\n",file=sys.stderr)
print(encoded_image,end='')
