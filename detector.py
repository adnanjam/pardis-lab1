import sys
from collections import OrderedDict

filename = sys.argv[1]

if filename is None:
    print("Please specify filename")
    exit(1)

lines = open(filename, 'r').readlines() # Read it as a list of strings

results_unsorted = {}

for line in lines:
    try:
            
        time, thread_id, operation_and_result, comment = line.split(",")

        op, res = operation_and_result.strip().split(" ")
        op = op.replace("[", "").replace("]", "")
        
        results_unsorted[int(time)] = {
            "thread": thread_id.strip(),
            "op": op,
            "res": res
        }
    except Exception as e:
        continue

# Sort keys ascending 
results = OrderedDict(sorted(results_unsorted.items()))

# Find failed contains after add:

for i in range(len(results.keys() )-1):
    cur = results[results.keys()[i]]
    nex = results[results.keys()[i+1]]

    
    if cur["op"] == "ADD" and  nex["op"] == "CONTAINS" and nex["res"] == "FAILED":
        print("Invalid operation found, aborting")
        exit(0)
    
    

print("No invalid operation found")