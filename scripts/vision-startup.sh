#headless mode -- redirect display output
#required because detectnet needs display and will fail without it
export DISPLAY=:0

VISION_HOME=/5700/vision
VISION_SCRIPTS=$VISION_HOME/scripts

THRESHOLD=0.7

#max out performance
sudo $VISION_SCRIPTS/jetson_clocks.sh

#launch detectnet and pipe unbuffered output to python to be sent to network tables
unbuffer $VISION_HOME/jetson-inference/build/aarch64/bin/detectnet-camera \
--prototxt=$VISION_HOME/models/tape/deploy.prototxt \
--model=$VISION_HOME/models/tape/snapshot.caffemodel \
--input_blob=data --output_cvg=coverage --output_bbox=bboxes --threshold=$THRESHOLD \
| python3 $VISION_SCRIPTS/bb2nt.py &

#bring down the camera exposure
v4l2-ctl -c exposure_auto=1
v4l2-ctl -c exposure_absolute=10

$VISION_SCRIPTS/stream-screen.sh