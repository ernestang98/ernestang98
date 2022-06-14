def kmp_search(pat, seq):

    prefix = [0] * len(pat)
    prefix = create_prefix(pat, prefix)

    pat_index = 0
    seq_index = 0

    position_list = []

    while seq_index < len(seq):
        
        if pat[pat_index] == seq[seq_index]:
            seq_index += 1
            pat_index += 1

        if pat_index == len(pat):
            position_list.append(seq_index - pat_index + 1)
            pat_index = prefix[pat_index - 1]

        else:
            if seq_index < len(seq) and pat[pat_index] != seq[seq_index]:
                if pat_index != 0:
                    pat_index = prefix[pat_index - 1]
                else:
                    seq_index += 1

    return position_list


def create_prefix(pattern, prefix_array):

    first_pointer = 0
    second_pointer = 1

    while second_pointer < len(pattern):

        if pattern[second_pointer] == pattern[first_pointer]:
            first_pointer += 1
            prefix_array[second_pointer] = first_pointer
            second_pointer += 1

        else:
            if first_pointer != 0:
                first_pointer = prefix_array[first_pointer - 1]
            else:
                prefix_array[second_pointer] = 0
                second_pointer += 1

    return prefix_array
