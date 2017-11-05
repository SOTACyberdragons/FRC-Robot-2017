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

#brightness (int)    : min=30 max=255 step=1 default=133 value=100
#exposure_absolute (int)    : min=5 max=20000 step=1 default=156 value=20
gear_brightness = '100'
peg_brightess = '30'

def parse_detect_string(detect_string):
		return [float(i) for i in iter(p.findall(detect_string))]

try:
	for line in iter(sys.stdin.readline, ''):
		if 'camera open for streaming' in line:
			print('camera open: set exposure for ' + current_model)
			os.system('v4l2-ctl -c exposure_auto=1\n'\
					'v4l2-ctl -c exposure_absolute=20\n' + \
					'v4l2-ctl -c brightness=' + \
					(peg_brightess if current_model == 'peg' else gear_brightness) + \
					'\nv4l2-ctl -l')
		elif line.startswith('bounding box 0') or \
			line.startswith('0 bounding'):
			table.putNumberArray('BBoxCoordinates', parse_detect_string(line))
		
		model = table.getString("model")
		if model != current_model:
			current_model = model
			if current_model == 'gear':
				print('switching to gear')
				os.system('/5700/vision/scripts/launch-gear.sh')
			elif current_model == 'peg':
				print('switching to peg')
				os.system('/5700/vision/scripts/launch-peg.sh')
				
		
except KeyboardInterrupt:
  	pass
