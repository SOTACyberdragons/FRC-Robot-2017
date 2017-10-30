import os, sys, re, time
from networktables import NetworkTables

#RoboPy recommends static IP, names may not work on the field
NetworkTables.initialize(server='10.57.0.2')

table = NetworkTables.getTable('vision')
current_model = 'peg'
#initialize the first time or get existing model named
table.putString('model', current_model)
#current_model = table.getString("switch-model")
#if not current_model:
#	current_model = 'peg'
#	table.putString('switch-model', current_model)

#Example detectnet string
#bounding box 0   (423.782074, 383.807373)  (569.935913, 452.460938)  w=146.153839  h=68.653564

p = re.compile(r"\d+\.\d+")

def parse_detect_string(detect_string):
		return [float(i) for i in iter(p.findall(detect_string))]

try:
	for line in iter(sys.stdin.readline, ''):
		if line.startswith('bounding box ') or \
			line.startswith('0 bounding'):
			table.putNumberArray('BBoxCoordinates', parse_detect_string(line))
		
		model = table.getString("model")
		if model != current_model:
			current_model = model
			if current_model == 'gear':
				os.system('/5700/vision/scripts/launch-gear.sh')
			elif current_model == 'peg':
				os.system('/5700/vision/scripts/launch-peg.sh')
				
		
except KeyboardInterrupt:
  	pass
