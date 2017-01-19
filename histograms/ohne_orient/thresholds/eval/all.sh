#!/bin/bash
let "counter = 15"
for file in eval_histograms_double_enhanced_rgb_100-1000_50_20_{5..125..5}; do
    head -q --lines=1 "${file}_${counter}_5.txt" >> all.txt
    echo $counter
    echo $file
    echo "${file}_${counter}"
    let "counter += 15"
done
