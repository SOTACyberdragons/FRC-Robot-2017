import os, sys, re, time
#from networktables import NetworkTables

#RoboPy recommends static IP, names may not work on the field
#NetworkTables.initialize(server='laghai.local')

#table = NetworkTables.getTable('vision')

current_model = 'peg'
#table.putString('switch model', current_model)

#Example detectnet string
#bounding box 0   (423.782074, 383.807373)  (569.935913, 452.460938)  w=146.153839  h=68.653564

p = re.compile(r"\d+\.\d+")

def parse_detect_string(detect_string):
		return [float(i) for i in iter(p.findall(detect_string))]

try:
	for line in iter(sys.stdin.readline, ''):
		#table.putStringArray('foo', parse_detect_string(line))
		print('foo')
		
		model = 'foo' #table.getString("switch model")
		if model != current_model:
			current_model = model
				
		
except KeyboardInterrupt:
  	pass
