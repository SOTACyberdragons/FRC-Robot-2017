#headless mode -- redirect display output
#required because detectnet needs display and will fail without it
export DISPLAY=:0

cd /5700/vision

#max out performance
sudo ./jetson_clocks.sh

#launch detectnet and pipe unbuffered output to python to be sent to network tables
unbuffer ./jetson-inference/build/aarch64/bin/detectnet-camera \
--prototxt=/5700/vision/models/gear/deploy.prototxt \
--model=/5700/vision/models/gear/snapshot.caffemodel \
--input_blob=data --output_cvg=coverage --output_bbox=bboxes --threshold=0.7 \
| python3 bb2nt.py &

#bring down the camera exposure
v4l2-ctl -c exposure_auto=1
v4l2-ctl -c exposure_absolute=10
