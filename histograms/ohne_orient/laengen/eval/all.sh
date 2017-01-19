#!/bin/bash
head -q --lines=1 "eval_histograms_double_enhanced_rgb_25-1000_50_20_33_100_5.txt" >> all.txt
head -q --lines=1 "eval_histograms_double_enhanced_rgb_50-1000_50_20_33_100_5.txt" >> all.txt
head -q --lines=1 "eval_histograms_double_enhanced_rgb_100-1000_50_20_33_100_5.txt" >> all.txt
for file in eval_histograms_double_enhanced_rgb_{200..800..200}-1000_50_20_33_100_5.txt; do
    head -q --lines=1 "${file}" >> all.txt
done
head -q --lines=1 "eval_histograms_double_enhanced_rgb_900-1000_50_20_33_100_5.txt" >> all.txt
