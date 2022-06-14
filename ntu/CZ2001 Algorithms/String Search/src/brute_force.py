def bf_search(query, seq):
    position_list = []
    # iterate through each index of the whole sequence that can be the first index of the query sequence
    for i in range(len(seq) - len(query) + 1):
        j = 0

        # check if the query sequence matches the sequence starting at index i
        while (j < len(query)) and (seq[i + j] == query[j]):
            j += 1

        # check if the total number of matched characters equals to length of query
        if j == len(query):
            position_list.append(i + 1)

    return position_list
