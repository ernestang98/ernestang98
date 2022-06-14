import math

BASE = 5

#-----searching for pattern
def rk_search(pattern, text):
    exists = False
    positions = []
    
    m = len(pattern)
    n = len(text)

    pattern_list = []
    for x in pattern:
        pattern_list.append(x)

    text_list = []
    for y in text:
        text_list.append(y)

    #convert each character to unique numbers
    #only queries with valid characters will be passed into the function, so no error handling here
    for i in range(m):
        if (pattern_list[i] == 'A'):
            pattern_list[i] = 0
        elif (pattern_list[i] == 'C'):
            pattern_list[i] = 1
        elif (pattern_list[i] == 'G'):
            pattern_list[i] = 2
        elif (pattern_list[i] == 'T'):
            pattern_list[i] = 3
        elif (pattern_list[i] == 'U'):
            pattern_list[i] = 4

    for i in range(n):
        if (text_list[i] == 'A'):
            text_list[i] = 0
        elif (text_list[i] == 'C'):
            text_list[i] = 1
        elif (text_list[i] == 'G'):
            text_list[i] = 2
        elif (text_list[i] == 'T'):
            text_list[i] = 3
        elif (text_list[i] == 'U'):
            text_list[i] = 4
    
    pattern = pattern_list
    text = text_list
    
    #create hash value for pattern
    pattern_hash = create_hash(pattern, m, BASE)

    #create hash value for current text
    text_hash = create_hash(text, m, BASE)

    #go through text
    for i in range(n - m + 1):
        #move on to next position in text
        if (i != 0):
            text_hash = recalculate_hash(text, i-1, i + m - 1, text_hash, m, BASE)
        if (pattern_hash == text_hash):
            exists = True
            positions.append(i+1)

    return positions


#-----calculate hash value
def create_hash(string, end, base):
    hash_value = 0
    for i in range(end):
        hash_value += string[i] * math.pow(base, end - i - 1)
    return hash_value


#-----calculate hash value for next position in text
def recalculate_hash(string, old_index, new_index, old_hash, pattern_len, base):
    new_hash = old_hash - (string[old_index] * math.pow(base, pattern_len - 1)) #drop value of dropped character
    new_hash *= base
    new_hash += string[new_index]
    return new_hash
