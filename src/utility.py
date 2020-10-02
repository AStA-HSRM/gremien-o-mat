#! /usr/bin/env python3
import os
import sys
from datetime import datetime

def get_script_path():
    return os.path.dirname(os.path.realpath(sys.argv[0]))

def getTime():
    return str(datetime.now())