# Lab 1

### Evaluation 1

1. ADD

	```
	LUT Slices | Delay
	8          | 5.725ns
	16         | 5.877ns
	32         | 6.181ns
	64         | 6.789ns
	```

2. MUL 

	```
	LUT Slices | Delay
	24         | 8.315ns
	48         | 8.315ns
	72         | 15.550ns
	96         | 13.845ns
	```

### Evaluation 2

1. ALU

	```
	LUT Slices | Delay
	16         | 6.856ns
	32         | 7.286ns
	48         | 7.931ns
	64         | 8.845ns
	```

### Notes

- Look up table (LUT) proportional to area, hence when plotting area to bits/delay, can just use LUT values.

- Make sure that the input files are in the same directory as the project file (simply importing it will not work/cause problems)

- To simulate behaviour model, make sure click on test bench

- To run synthesize - XST, set the verilog program (not the test bench) as the top module