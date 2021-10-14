import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

data  = pd.read_csv("results-3.csv", header=None, names=['nodes', 'time', "normal", "ms-time", "experiment"])


normal = 0

title = f"""
{"NORMAL DISTRIBUTION" if normal == 1 else "UNIFORM DISTRIBUTION"}
A: 0.10 remove, 0.10 add, 0.80 contains
B: 0.50 remove, 0.50 add, 0.00 contains
C: 0.25 remove, 0.25 add, 0.50 contains
D: 0.05 remove, 0.05 add, 0.90 contains
"""

# print(data[(data["experiment"] == "A")])

# 1 line per algo
# X: threads 
# Y: time
# n: constant


plt.figure()
for experiment in ["A", "B", "C", "D"]:
    line = data[["time", "nodes"]][(data["experiment"] == experiment) & (data["normal"] == normal)].astype(float)
    # print(line)
    plt.plot(line["nodes"], line["time"], label=experiment)

plt.xlabel('Nodes')
# Set the y axis label of the current axis.
plt.ylabel('Time')
# Set a title of the current axes.
plt.title(title)
# show a legend on the plot
plt.legend()
# Display a figure.
# plt.show()
plt.savefig(f'tegner-{"normal" if normal == 1 else "uniform"}.png', bbox_inches='tight')