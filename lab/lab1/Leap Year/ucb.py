import os

junit = "/home/ff/cs61b/lib/junit.jar"
hamcrest = "/home/ff/cs61b/lib/hamcrest-core.jar"
ucb = "/home/ff/cs61b/lib/ucb.jar"

login = raw_input("Please enter your two letter login (cs61b-??): ")

command = "scp cs61b-{0}@cory.eecs.berkeley.edu:{1} cs61b-{0}@cory.eecs.berkeley.edu:{2} cs61b-{0}@cory.eecs.berkeley.edu:{3} .".format(login, junit, ucb, hamcrest)

os.system(command)