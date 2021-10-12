import sys
from collections import OrderedDict

filename = sys.argv[1]

if filename is None:
    print("Please specify filename")
    exit(1)

lines = open(filename, 'r').readlines() # Read it as a list of strings

results_unsorted = {}

for i in range(len(lines)):
    try:
        # [0] pool-1-thread-3,
        # [1] ADD,
        # [2] 6325520880534,
        # [3] LIN_ADD,
        # [4] 6325520881820,
        # [5] RET,
        # [6] false,
        # [7] 6325520882324

        data = lines[i].split(",")
        time = int(data[4])

        results_unsorted[time] = {
            "op": data[1].strip(),
            "res": data[6].strip()
        }

    except Exception as e:
        continue

# Sort keys ascending
results = OrderedDict(sorted(results_unsorted.items()))

# Find failed contains after add:
invalid_count = 0
total = len(results.keys())

for i in range(total-1):
    cur = results[results.keys()[i]]
    nex = results[results.keys()[i+1]]

    if cur["op"] == "ADD" and  nex["op"] == "CONTAINS" and nex["res"] == "false":
#         print("Invalid operation found, aborting")
#         print(cur)
#         print(nex)
        invalid_count += 1


    elif cur["op"] == "REMOVE" and  nex["op"] == "CONTAINS" and nex["res"] == "true":
#         print("Invalid operation found, aborting")
#         print(cur)
#         print(nex)
        invalid_count  += 1


ratio = float(invalid_count) / float(total)

print("Total: ", total, ", invalid: ", invalid_count, ", ratio: " + str(ratio))

# print("No invalid operation found")
