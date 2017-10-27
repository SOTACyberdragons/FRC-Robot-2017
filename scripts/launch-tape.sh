sudo ./jetson_clocks.sh
unbuffer ./jetson-inference/build/aarch64/bin/detectnet-camera --prototxt=/5700/vision/models/tape/deploy.prototxt\
 --model=/5700/vision/models/tape/snapshot.caffemodel\
  --input_blob=data --output_cvg=coverage --output_bbox=bboxes --threshold=0.9 | python3 bb2nt.py
