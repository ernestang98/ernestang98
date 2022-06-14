### Lab 2 learning points

- [PyTorch 9hrs Crash Course](https://www.youtube.com/watch?v=GIsg-ZUy0MY)
- [Load `.mat` files](https://www.codegrepper.com/code-examples/python/load+mat+file+in+jupyter)
- [Python map() functions](https://www.geeksforgeeks.org/python-map-function/)
- [Occurences of a value in numpy array](https://thispointer.com/count-occurrences-of-a-value-in-numpy-array-in-python/)

- [Numpy cannot cast data types float64 to s32](https://stackoverflow.com/questions/34173101/numpy-dot-typeerror-cannot-cast-array-data-from-dtypefloat64-to-dtypes32)
  - common problem seen also when dealing with pytorch tensors of different data types

- [Conversion of data type of numpy arrays](https://www.kite.com/python/answers/how-to-convert-a-numpy-array-of-floats-into-integers-in-python)
- [RuntimeError: dimension out of range (expected to be in range of -1, 0, but got 1)](https://stackoverflow.com/questions/48377214/runtimeerror-dimension-out-of-range-expected-to-be-in-range-of-1-0-but-go)
  - Cause due to mismatch in inputs and outputs of softmax activation function
  - You can change the dim to [-1, 0] if the error is derived from the line `F.softmax(x,dim=1)`
  - If error is not derived from the line, then probably from other areas (e.g. loss function calculation as seen above)
  - Depending on the area (usually mismatches in the structure of actual input torch tensors to torch functions/expected input torch tensors), debug and fix the structure of these input torch tensors
  - [Example to fixing this on GitHub issues](https://github.com/pytorch/pytorch/issues/5554)
  - [Pytorch Forum Discussion](https://discuss.pytorch.org/t/indexerror-dimension-out-of-range-expected-to-be-in-range-of-1-0-but-got-1/54267/8)
  - [Chinese website forum](https://blog.csdn.net/weixin_43436958/article/details/106907345)
  - [Some forum](https://clay-atlas.com/us/blog/2021/07/22/pytorch-en-index-error-dimension-out-of-range/)

- [][mat1 and mat2 shapes cannot be multiplied]occurs during forward() or backward(), mismatch between input tensors or output tensors (mismatch between expected and actual sizes/dimensions of tensors)
  - [mat1 and mat2 shapes cannot be multiplied #1](https://stackoverflow.com/questions/66337378/mat1-and-mat2-shapes-cannot-be-multiplied)
  - [mat1 and mat2 shapes cannot be multiplied #2](https://discuss.pytorch.org/t/runtimeerror-mat1-and-mat2-shapes-cannot-be-multiplied-64x13056-and-153600x2048/101315/8)
  - [mat1 and mat2 shapes cannot be multiplied #3](https://stackoverflow.com/questions/66337378/mat1-and-mat2-shapes-cannot-be-multiplied)
  - [RuntimeError: shape '[16, 400]' is invalid for input of size 9600](https://stackoverflow.com/questions/60439570/beginner-pytorch-runtimeerror-shape-16-400-is-invalid-for-input-of-size)
  - [ValueError: Expected target size 32, 7, got torch.Size [32]](https://discuss.pytorch.org/t/valueerror-expected-target-size-32-7-got-torch-size-32/42409)
- [What are Forward Feed Neural Networks?](https://medium.com/biaslyai/pytorch-introduction-to-neural-network-feedforward-neural-network-model-e7231cff47cb)
- [Different activation functions](https://stats.stackexchange.com/questions/218752/relu-vs-sigmoid-vs-softmax-as-hidden-layer-neurons)
- Following errors happens when you try to 'wrongly' create torch.Tensors (mostly due to wrong formatting or wrong  tensor data type usage when we initialise and assign values to our tensors)
  - [TypeError: only integer tensors of a single element can be converted to an index](https://discuss.pytorch.org/t/typeerror-only-integer-tensors-of-a-single-element-can-be-converted-to-an-index/45641)
  - [ValueError: only one element tensors can be converted to Python scalars](https://discuss.pytorch.org/t/valueerror-only-one-element-tensors-can-be-converted-to-python-scalars/59800/8)
  - [convert list into an array, get ValueError: only one element tensors can be converted to Python scalars](https://stackoverflow.com/questions/52074153/cannot-convert-list-to-array-valueerror-only-one-element-tensors-can-be-conver)
  - https://discuss.pytorch.org/t/only-one-element-tensors-can-be-converted-to-python-scalars/107846
- [Convert A Python List To A PyTorch Tensor](https://www.aiworkbox.com/lessons/convert-list-to-pytorch-tensor)
- [RuntimeError: expected scalar type Long but found Float](https://stackoverflow.com/questions/60440292/runtimeerror-expected-scalar-type-long-but-found-float)
  - Wrong torch tensor data type used 
  - [Cast tensor to another type](https://discuss.pytorch.org/t/how-to-cast-a-tensor-to-another-type/2713)
  - [Torch Tensor data type conversion](https://stackoverflow.com/questions/56741087/how-to-fix-runtimeerror-expected-object-of-scalar-type-float-but-got-scalar-typ)
  - [Stackoverflow Torch Tensor Data types](https://stackoverflow.com/questions/60440292/runtimeerror-expected-scalar-type-long-but-found-float)
  - [PyTorch Doc Torch Tensor Data types](https://pytorch.org/docs/stable/tensors.html)
- [Normalizing output tensors](https://discuss.pytorch.org/t/how-to-normalize-the-output-tensor-to-0-1-and-then-calculate-the-ssim-between-channels-as-loss/58102)
- [Epoch vs Batch Size vs Iterations #1](https://towardsdatascience.com/epoch-vs-iterations-vs-batch-size-4dfb9c7ce9c9)
- [Epoch vs Batch Size vs Iterations #2](https://stats.stackexchange.com/questions/117919/what-are-the-differences-between-epoch-batch-and-minibatch)
- [Log Softmax with torch.LongTensor](https://github.com/pytorch/pytorch/issues/14224)

- [Categorical Cross Entropy (CCE) loss function #1](https://discuss.pytorch.org/t/categorical-cross-entropy-loss-function-equivalent-in-pytorch/85165/3)
- [Categorical Cross Entropy (CCE) loss function #2](https://stackoverflow.com/questions/58923416/pytorch-categorical-cross-entropy-loss-function-behaviour)
- [CrossEntropyLoss input and label data types](https://github.com/pytorch/pytorch/issues/40388)
- [RuntimeError: element 0 of variables does not require grad and does not have a grad_fn](https://discuss.pytorch.org/t/runtimeerror-element-0-of-variables-does-not-require-grad-and-does-not-have-a-grad-fn/11074)
- [map() alternative for torch tensors](https://discuss.pytorch.org/t/fast-way-to-use-map-in-pytorch/70814/9)
- [Softmax Activation function dim parameter](https://stackoverflow.com/questions/49036993/pytorch-softmax-what-dimension-to-use)
- [Indexing a torch tensor](https://pytorch.org/docs/stable/generated/torch.index_select.html)

