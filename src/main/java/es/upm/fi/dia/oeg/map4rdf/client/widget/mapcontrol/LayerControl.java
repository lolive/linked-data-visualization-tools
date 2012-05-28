/**
 * Copyright (c) 2011 Ontology Engineering Group, 
 * Departamento de Inteligencia Artificial,
 * Facultad de Informetica, Universidad 
 * Politecnica de Madrid, Spain
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package es.upm.fi.dia.oeg.map4rdf.client.widget.mapcontrol;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.presenter.client.Display;

import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.ControlAnchor;
import com.google.gwt.maps.client.control.ControlPosition;
import com.google.gwt.maps.client.control.Control.CustomControl;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Alexander De Leon
 */
public class LayerControl extends CustomControl implements MapControl {

	private final List<Overlay> overlays = new ArrayList<Overlay>();
	private final FlowPanel emptyControlUi = new FlowPanel();
	private Display display;
	private MapWidget map;

	public LayerControl() {
		super(new ControlPosition(ControlAnchor.BOTTOM_RIGHT, 0, 0), false, false);
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	public void setDisplay(Display display) {
		this.display = display;
	}

	@Override
	public CustomControl getCustomControl() {
		return this;
	}

	protected void addOverlay(Overlay overlay) {
		overlays.add(overlay);
		map.addOverlay(overlay);
	}

	public void clear() {
		if (display != null) {
			display.startProcessing();
		}

		for (Overlay o : overlays) {
			map.removeOverlay(o);
		}
		overlays.clear();
		
		if (map.getInfoWindow().isVisible()){
			map.getInfoWindow().setVisible(false);
		}
		
		if (display != null) {
			display.stopProcessing();
		}
	}

	protected void disable() {
		clear();
	}

	protected MapWidget getMap() {
		return map;
	}

	protected Display getDisplay() {
		return display;
	}

	@Override
	protected Widget initialize(MapWidget map) {
		this.map = map;
		return emptyControlUi;
	}
	protected InfoWindow getWindow() {
		return this.map.getInfoWindow();
	}

}
