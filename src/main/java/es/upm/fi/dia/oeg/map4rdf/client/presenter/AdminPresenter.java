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
package es.upm.fi.dia.oeg.map4rdf.client.presenter;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.place.Place;
import net.customware.gwt.presenter.client.place.PlaceRequest;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.upm.fi.dia.oeg.map4rdf.client.navigation.Places;
import es.upm.fi.dia.oeg.map4rdf.client.presenter.AdminPresenter.Display;
import es.upm.fi.dia.oeg.map4rdf.client.services.IDBService;
import es.upm.fi.dia.oeg.map4rdf.client.services.IDBServiceAsync;
import es.upm.fi.dia.oeg.map4rdf.client.services.ISessionsService;
import es.upm.fi.dia.oeg.map4rdf.client.services.ISessionsServiceAsync;
import es.upm.fi.dia.oeg.map4rdf.share.ConfigPropertie;
import es.upm.fi.dia.oeg.map4rdf.share.conf.ParameterNames;
import java.util.ArrayList;
import java.util.List;
import name.alexdeleon.lib.gwtblocks.client.PagePresenter;
import net.customware.gwt.presenter.client.place.PlaceChangedEvent;

/**
 * @author Alexander De Leon
 */
@Singleton
public class AdminPresenter extends  PagePresenter<AdminPresenter.Display>  {

	public interface Display extends WidgetDisplay {

        public void clear();
        public Button getLogoutButton();
        public void setVisibility(Boolean visibility);
        public void fullfilForm(List<ConfigPropertie> result);

	}
    private ISessionsServiceAsync sessionsService;
    private IDBServiceAsync dbService;
    
	private final DispatchAsync dispatchAsync;
    //private IDBServiceAsync propertiesServiceAsync;
	@Inject
	public AdminPresenter(Display display, EventBus eventBus, DispatchAsync dispatchAsync) {
		super(display, eventBus);
		this.dispatchAsync = dispatchAsync;
        sessionsService = GWT.create(ISessionsService.class);
        dbService = GWT.create(IDBService.class);
    }

	/* -------------- Presenter callbacks -- */
	@Override
	protected void onBind() {
		display.getLogoutButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                
                    sessionsService.logout(new AsyncCallback() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Check your database connection");
                    }

                    @Override
                    public void onSuccess(Object result) {
                        Cookies.removeCookie("admin");
                        eventBus.fireEvent(new PlaceChangedEvent(Places.DASHBOARD.request()));
                        Window.Location.reload();
                    }
                });
            }
        });
	}

	@Override
	protected void onUnbind() {
		// TODO Auto-generated method stub

	}

    
        @Override
    protected void onRefreshDisplay() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void onRevealDisplay() {
        
    }

    @Override
    public Place getPlace() {
         return Places.ADMIN;
    }

    @Override
    protected void onPlaceRequest(PlaceRequest request) {
        display.setVisibility(Boolean.FALSE);
        if(Cookies.getCookie("admin") != null && Cookies.getCookie("admin").toString().equals("true")) {
            display.setVisibility(Boolean.TRUE);
            ArrayList<String> paramNames = new ArrayList<String>();
            paramNames.add(ParameterNames.ENDPOINT_URL);
            paramNames.add(ParameterNames.FACETS_AUTO);
            paramNames.add(ParameterNames.GEOMETRY_MODEL);
            paramNames.add(ParameterNames.GOOGLE_MAPS_API_KEY);
           
            dbService.getValues(paramNames,new AsyncCallback<List<ConfigPropertie>>(){

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage().toString());
                }

                @Override
                public void onSuccess(List<ConfigPropertie> result) {
                    display.fullfilForm(result);
                }
                
            });
        }
        else {
          eventBus.fireEvent(new PlaceChangedEvent(Places.LOGIN.request()));
          Window.Location.reload();
        }
    }

}
