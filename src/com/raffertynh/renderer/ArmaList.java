package com.raffertynh.renderer;

import java.awt.Rectangle;

import javax.swing.JList;

public class ArmaList extends JList {


	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 16;
	}
}
