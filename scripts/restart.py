import subprocess
import signal
import os
import sys


process_name = "detectnet"
#log_file_name = sys.argv[2]


proc = subprocess.Popen(["pgrep", process_name], stdout=subprocess.PIPE) 

# Kill process.
for pid in proc.stdout:
    os.kill(int(pid), signal.SIGTERM)
    # Check if the process that we killed is alive.
    #try: 
       #os.kill(int(pid), 0)
       #raise Exception("""wasn't able to kill the process 
       #                   HINT:use signal.SIGKILL or signal.SIGABORT""")
    #except OSError as ex:
    #   continue

#linux: check to see running process/threads: /proc/<PID>/task/ 
# Save old logging file and create a new one.
#os.system("cp {0} '{0}-dup-{1}'".format(log_file_name, dt.now()))

# Empty the logging file.
#with open(log_file_name, "w") as f:
#    pass

# Run the process again.
os.system("./launch-gear.sh") 
# you can use os.exec* if you want to replace this process with the new one which i think is much better in this case.

# the os.system() or os.exec* call will failed if something go wrong like this you can check if the process is runninh again.
