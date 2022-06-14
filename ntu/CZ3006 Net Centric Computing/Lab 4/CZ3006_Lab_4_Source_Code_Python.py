"""
Import libraries
"""

import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt


"""
Read data 
"""

df = pd.read_csv(
    'Data_2.csv', 
    index_col = False,
    names = [
        'type',
        'flow_agent_addr',
        'inputPort',
        'outputPort',
        'src_MAC',
        'dst_MAC',
        'eth_type',
        'in_vlan',
        'out_vlan',
        'src_IP',
        'dst_IP',
        'IP_Protocol',
        'ip_tos',
        'ip_ttl',
        'src_port',
        'dst_port',
        'tcp_flags',
        'packet_size',
        'IP_size',
        'sampling_rate'
    ]
)

df.head()


"""
Basic Information:
Top 5 Talkers. (ie sender nodes)
Top 5 Listeners (ie receiving node)
Top 5 applications
Total estimated traffic
Proportion of TCP and UDP packets
"""

top_5_talkers = df['src_IP'].value_counts()[:5]
top_5_listeners = df['dst_IP'].value_counts()[:5]
top_5_applications = df['dst_port'].value_counts()[:5]
total_estimated_traffic = df['IP_size'].sum()

tcp_count = df['IP_Protocol'].value_counts().get(6)
udp_count = df['IP_Protocol'].value_counts().get(17)
total_count = df['IP_Protocol'].value_counts().sum()
tcp_proportion = tcp_count / total_count
udp_proportion = udp_count / total_count

'''
Print out some statistics
'''
print('Value Counts for IP Protocols:\n{}\n'.format(df['IP_Protocol'].value_counts()))


'''
Answers:
'''

print('Top 5 talkers (IP):\n{}\n'.format(top_5_talkers))
print('Top 5 listeners (IP):\n{}\n'.format(top_5_listeners))
print('Top 5 applications:\n{}\n'.format(top_5_applications))
print('Total estimated traffic:\n{} bytes\n'.format(total_estimated_traffic))
print('Proportion of TCP and UDP packets:\nTCP Proportion: {}\nUDP Proportion: {}\n'.format(tcp_proportion, udp_proportion))


"""
Additional Information:
Top 5 communication pair.
Visualizing the communication between different IP hosts. 
Any other stuff.
"""

'''
Top 5 communication pair


Example:
src | dest
----|-----
a   | b
----|-----
c   | d
----|-----
b   | a
'''

communication_pair = {}

for index, data_point in df.iterrows():
    communication_pair_1 = data_point['src_IP'] + '/' + data_point['dst_IP'] # word1 a/b, {}|| word1 c/d      || word1 b/a
    communication_pair_2 = data_point['dst_IP'] + '/' + data_point['src_IP'] # word2 b/a, {}|| word2 d/c      || word2 a/b
    if communication_pair_1 in communication_pair.keys():                    # {a/b:1}      || {a/b:1, c/d:1} || {a/b:2, c/d:1}
        communication_pair[communication_pair_1] += 1
    elif communication_pair_2 in communication_pair.keys():
        communication_pair[communication_pair_2] += 1
    else:
        communication_pair[communication_pair_1]=1                 

communication_pair_sorted = sorted([(k,v) for k,v in communication_pair.items()], key= lambda x: x[1], reverse=True)
top_5_communication_pair_sorted = communication_pair_sorted[:5]
print('Top 5 communication pairs:\n')
print('----------------------------------------------------------')
for index, value in enumerate(top_5_communication_pair_sorted):
    print('| Pair {index} - {pair} | Traffic - {traffic} |'.format(index = index + 1, pair = value[0], traffic = value[1]))
    print('----------------------------------------------------------')


"""
Measuring the average traffic per 100 millisecond (0.1 seconds) to determine peak network traffic

Assumption 1: Estimated total traffic over a monitored period of 10 seconds
Assumption 2: Packet transmission is spread and distributed equally over the 10 seconds
Assumption 3: Assume the dataset is a time-series dataset
"""

import matplotlib.pyplot as plt
import numpy as np

df.index.names = ['id']
df_average_traffic_per_100_ms = pd.DataFrame(columns = ['timestamp', 'total_traffic'])
split_df_into_100_segments = np.array_split(df, 100)

for i in range(len(split_df_into_100_segments)):
    indexed_split_df_into_100_segments = split_df_into_100_segments[i]
    total_traffic_of_indexed_split_df_into_100_segments = indexed_split_df_into_100_segments['IP_size'].sum() * 1000 * 10**(-6)
    df_average_traffic_per_100_ms.loc[len(df_average_traffic_per_100_ms)] = [i/10, total_traffic_of_indexed_split_df_into_100_segments]

df_average_traffic_per_100_ms_max_traffic = round(df_average_traffic_per_100_ms['total_traffic'].max(), 3)
print("Max traffic: {} MB".format(df_average_traffic_per_100_ms_max_traffic))

df_average_traffic_per_100_ms_mean_traffic = round(df_average_traffic_per_100_ms['total_traffic'].mean(), 3)
print("Mean traffic: {} MB".format(df_average_traffic_per_100_ms_mean_traffic))

df_average_traffic_per_100_ms_median_traffic = round(df_average_traffic_per_100_ms['total_traffic'].median(), 3)
print("Median traffic: {} MB".format(df_average_traffic_per_100_ms_median_traffic))

ax = plt.gca()
df_average_traffic_per_100_ms.plot(kind = 'line', x = 'timestamp', y = 'total_traffic', ax = ax)
plt.show()


"""
Measuring the average traffic per second

Assumption 1: Estimated total traffic over a monitored period of 10 seconds
Assumption 2: Packet transmission is spread and distributed equally over the 10 seconds
Assumption 3: Assume the dataset is a time-series dataset
"""

import matplotlib.pyplot as plt
import numpy as np

df.index.names = ['id']
df_average_traffic_second = pd.DataFrame(columns = ['timestamp', 'total_traffic'])
split_df_into_10_segments = np.array_split(df, 10)

for i in range(len(split_df_into_10_segments)):
    indexed_split_df_into_10_segments = split_df_into_10_segments[i]
    total_traffic_of_indexed_split_df_into_10_segments = indexed_split_df_into_10_segments['IP_size'].sum() * 1000 * 10**(-6)
    df_average_traffic_second.loc[len(df_average_traffic_second)] = [i/10, total_traffic_of_indexed_split_df_into_10_segments]

df_average_traffic_second_max_traffic = round(df_average_traffic_second['total_traffic'].max(), 3)
print("Max traffic: {} MB".format(df_average_traffic_second_max_traffic))

df_average_traffic_second_mean_traffic = round(df_average_traffic_second['total_traffic'].mean(), 3)
print("Mean traffic: {} MB".format(df_average_traffic_second_mean_traffic))

df_average_traffic_second_median_traffic = round(df_average_traffic_second['total_traffic'].median(), 3)
print("Median traffic: {} MB".format(df_average_traffic_second_median_traffic))

ax = plt.gca()
df_average_tr


'''
Visualizing the communication between different IP hosts 1 (Bar Chart)
'''

print('Network visualised in Bar Chart:\n')
pd.DataFrame(list(list(zip(*communication_pair_sorted))[1][0:100]), list(list(zip(*communication_pair_sorted))[0][0:100])).plot.barh(figsize=(100,100), colormap='Spectral');


'''
Visualizing the communication between different IP hosts 2 (Graph Network)
'''

graph = nx.Graph()
node_size = []
nodes = list(set(df['src_IP'].tolist() + df['dst_IP'].tolist()))
graph.add_nodes_from(nodes)

for p, n in communication_pair_sorted:
    graph.add_edge(p.split('/')[0], p.split('/')[1], weight = n)
    
for node in nodes:
    if graph.degree(node, weight='weight') < 25:
        node_size.append(5)
    elif graph.degree(node, weight='weight') < 50:
        node_size.append(10)
    elif graph.degree(node, weight='weight') < 75:
        node_size.append(15)
    elif graph.degree(node, weight='weight') < 100:
        node_size.append(20)
    elif graph.degree(node, weight='weight') < 125:
        node_size.append(25)
    else:
        node_size.append(30)
        
edges = graph.edges()
weights = [graph[u][v]['weight']/500 for u, v in edges]
print('Network visualised using Graph Visualisations v1:\n')
nx.draw_spring(graph, node_size = node_size, node_color = range(len(nodes)), width = weights, cmap = plt.cm.bwr)