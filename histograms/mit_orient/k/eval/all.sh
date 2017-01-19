#!/bin/bash
head -q --lines=1 "eval_histograms_double_enhanced_rgb_100-1000_50_20_33_100_2.txt" >> all.txt
for file in eval_histograms_double_enhanced_rgb_100-1000_50_20_33_100_{5..20..5}.txt; do
    head -q --lines=1 "${file}" >> all.txt
done
head -q --lines=1 "eval_histograms_double_enhanced_rgb_100-1000_50_20_33_100_30.txt" >> all.txt
