package com.cv4j.core.spatial.conv;

import com.cv4j.core.datamodel.ColorProcessor;
import com.cv4j.core.datamodel.ImageProcessor;
import com.cv4j.core.filters.CommonFilter;

/***
 * can iteration this operation multiple, make it more blur
 */
public class ConvolutionHVFilter implements CommonFilter {
	@Override
	public ImageProcessor filter(ImageProcessor src) {

		if (!(src instanceof ColorProcessor)) return src;

		int width = src.getWidth();
		int height = src.getHeight();
		int total = width*height;
		byte[] R = ((ColorProcessor)src).getRed();
		byte[] G = ((ColorProcessor)src).getGreen();
		byte[] B = ((ColorProcessor)src).getBlue();
		byte[][] output = new byte[3][total];
		int r=0, g=0, b=0;
		for(int row=0; row<height; row++) {
			int offset = row*width;
			for(int col=1; col<width-1; col++) {
				int sr=0, sg=0, sb=0; 
				for(int j=-1; j<=1; j++) {
					int coffset = j+col;
					sr += R[offset+coffset]&0xff;
					sg += G[offset+coffset]&0xff;
					sb += B[offset+coffset]&0xff;
				}
				r = sr / 3;
				g = sg / 3;
				b = sb / 3;
				output[0][offset+col]=(byte)r;
				output[1][offset+col]=(byte)g;
				output[2][offset+col]=(byte)b;
			}
		}
		
		// Y 方向
		for(int col=0; col<width; col++) {
			int coffset = col;
			for(int row=1; row<height-1; row++) {
				int sr=0, sg=0, sb=0; 
				for(int j=-1; j<=1; j++) {
					int roffset = j+row;
					sr += output[0][roffset*width+coffset]&0xff;
					sg += output[1][roffset*width+coffset]&0xff;
					sb += output[2][roffset*width+coffset]&0xff;
				}
				r = sr / 3;
				g = sg / 3;
				b = sb / 3;
				R[row*width+col]=(byte)r;
				G[row*width+col]=(byte)g;
				B[row*width+col]=(byte)b;
			}
		}
		output[0] = null;
		output[1] = null;
		output[2] = null;
		output = null;
		return src;
	}

}
