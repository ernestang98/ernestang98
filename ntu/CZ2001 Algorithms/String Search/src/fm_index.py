import pandas as pd
from collections import Counter


def bwt(dna):
    dna = dna + "$"
    matrix = sorted([dna[i:] + dna[:i] for i in range(len(dna))])
    F = ''.join(matrix[i][0] for i in range(len(matrix)))
    L = ''.join(matrix[i][len(dna) - 1] for i in range(len(matrix)))
    return F, L


def count(pattern, cc, occ):
    pattern = list(pattern)

    # setting initial range
    start = cc.loc["C[c]", pattern[-1]] + 1
    temp = cc.columns.get_loc(pattern[-1])
    if temp < 4:
        temp += 1
        end = cc.iloc[0, temp]
    else:
        end = occ.columns[-1]

    # irriteration to narrow the range
    for char in reversed(pattern[:-1]):
        if start > end:
            return False # pattern is not in input string, immediately break out of function
        if start > 1:
            start -= 1
        start = cc.loc["C[c]", char] + occ.loc[char, start] +1
        end = cc.loc["C[c]", char] + occ.loc[char, end]

    return range(start, end+1)


def locate(i_L, L, cc, occ):
    i_actual = 1
    while L[i_L-1] != "$":
        i_L =  cc.loc["C[c]", L[i_L-1]] + occ.loc[L[i_L-1], i_L] # apply LF mapping on i_L
        i_actual += 1
    return i_actual


def fm_search(pattern, text):
    dna_seq = str(text)
    F, L = bwt(dna_seq)

    def cc(L):
        L = list(L)
        keys = list(Counter(L).keys())
        values = list(Counter(L).values())
        temp1 = values[1]
        temp2 = values[2]
        temp3 = values[3]
        values[0] = 0
        values[1] = 1
        values[2] = values[1] + temp1
        values[3] = values[2] + temp2
        values[4] = values[3] + temp3
        return pd.DataFrame([values], columns=keys, index=["C[c]"])

    def occ(L):
        col_count = 1
        col_list = []
        # both row_list and char_counts are lists with 5 elements
        # each element corresponds to "$", "A", "C", "G" and "T" respectively
        row_list = [[], [], [], [], []]
        char_counts = [0, 0, 0, 0, 0]

        for char in L:
            # increases the char_counts
            if char == "$":
                char_counts[0] += 1
            elif char == "A":
                char_counts[1] += 1
            elif char == "C":
                char_counts[2] += 1
            elif char == "G":
                char_counts[3] += 1
            elif char == "T":
                char_counts[4] += 1
            # appends char_counts to row_list
            for i in range(0, 5):
                row_list[i].append(char_counts[i])

            # appends col_count to col_list
            col_list.append(col_count)
            col_count += 1

        return pd.DataFrame(row_list, columns=col_list, index=["$", "A", "C", "G", "T"])

    cc = cc(F)
    occ = occ(L)
    query_seq = str(pattern)
    F_range = count(query_seq, cc, occ)
    index_list = []
    if F_range:
        for i in F_range:
            index_list.append(locate(i, L, cc, occ))

    return index_list
