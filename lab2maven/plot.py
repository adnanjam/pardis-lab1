import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

data  = pd.read_csv("results-tegner.csv",  header=None, names=['threads', 'n', "algo", "time"])


thread_counts = data["threads"][~data["threads"].duplicated()]
array_sizes =  data["n"][~data["n"].duplicated()]
algos =  data["algo"][~data["algo"].duplicated()]


# 1 line per algo
# X: size 
# Y: time
# thread: constant

tc = 3

plt.figure()

for algo in algos:
    line = data[["time", "n"]][(data.threads == tc) & (data.algo == algo)].astype(float)
    plt.plot(line["n"],line["time"], label=algo)

plt.xlabel('Input size')
# Set the y axis label of the current axis.
plt.ylabel('Time')
# Set a title of the current axes.
plt.title('Thread count = 3')
# show a legend on the plot
plt.legend()
# Display a figure.
plt.savefig('tegner-by-input-size.png')



# 1 line per algo
# X: threads 
# Y: time
# n: constant


for n in [1000, 10000, 100000, 1000000]:
    plt.figure()
    for algo in algos:

        line = data[["time", "threads"]][(data.n == n) & (data.algo == algo)].astype(float)
        plt.plot(line["threads"], np.log(line["time"]/1e9), label=algo)

    plt.xlabel('Threads')
    # Set the y axis label of the current axis.
    plt.ylabel('Time')
    # Set a title of the current axes.
    plt.title('Input size ' +str(n))
    # show a legend on the plot
    plt.legend()
    # Display a figure.
    plt.savefig(f'tegner-by-threads-{n}.png')