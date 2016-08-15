"""
This script is used to in the automated test to generate random test inputs.
Usage:
    python random_input.py source target

Parameters
    source: Database folder of test inputs
    target: Target folder where newly randomly generated inputs are stored

"""
import os
import sys
import random
import shutil


#Arguments checking

print "Checking argumens..."

if not os.path.isdir(sys.argv[1]):
    sys.exit("Source %s is not a directory or does not exists, please enter another path"%sys.argv[2])


if os.path.exists(sys.argv[2]):
    sys.exit("File or directory %s already exists, please enter another path"%sys.argv[2])
else:
    os.makedirs(sys.argv[2])

print "Random sampling..."
inputs = os.listdir(sys.argv[1])
samples = random.sample(inputs, random.randint(1, len(inputs)))

print "Copying sampled files to new directory..."

for file in samples:
    shutil.copy(os.path.join(sys.argv[1], file), sys.argv[2])

print "Done."


