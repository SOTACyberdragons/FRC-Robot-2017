import os, sys, re, time
from networktables import NetworkTables

NetworkTables.initialize(server='laghai.local')

table = NetworkTables.getTable('jetson')

#Example detectnet string
#bounding box 0   (423.782074, 383.807373)  (569.935913, 452.460938)  w=146.153839  h=68.653564

p = re.compile(r"\d*\.\d*")

def parse_detect_string(detect_string):
	return [float(i) for i in iter(p.findall(detect_string))]

try:
	for line in iter(sys.stdin.readline, ''):
		print(time.time())
		table.putNumberArray('detect_float', parse_detect_string(line.strip('\n')))
		print(parse_detect_string(line.strip('\n')))
		print(line, end='')
		print(time.time())
except KeyboardInterrupt:
  	pass