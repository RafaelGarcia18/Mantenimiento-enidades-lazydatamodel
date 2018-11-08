/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.ModeloParteFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.ModeloParte;

/**
 *
 * @author rafael
 */
@Named(value = "frmModeloParte")
@ViewScoped
public class FrmModeloParte implements Serializable {

    @EJB
    private ModeloParteFacade service;
    private ModeloParte selectModeloParte;
    private EstadoCRUD estado;
    private LazyDataModel<ModeloParte> lazyModel;
    private List<ModeloParte> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<ModeloParte>() {
            @Override
            public Object getRowKey(ModeloParte object) {
                if (object != null) {
                    return object.getIdModelo();
                }
                return null;
            }

            @Override
            public ModeloParte getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Long search = new Long(rowKey);
                        for (ModeloParte tu : (List<ModeloParte>) getWrappedData()) {
                            if (tu.getIdModeloParte().compareTo(search) == 0) {
                                return tu;
                            }
                        }
                    } catch (NumberFormatException e) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                    }
                }
                return null;
            }
            
            @Override
            public List<ModeloParte> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                dataSource = new ArrayList<>();
                try {
                    if (service != null) {
                        this.setRowCount(service.count());
                        dataSource = service.findRange(first, pageSize);
                    }
                } catch (Exception e) {
                    System.out.println("Excepcion" + e.getMessage());
                }
                return dataSource;
            }

            @Override
            public int getRowIndex() {
                return super.getRowIndex();
            }
        };
    }

    public void onRowSelect(SelectEvent event) {
        selectModeloParte = (ModeloParte) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectModeloParte = new ModeloParte();
        this.lazyModel.setRowIndex(-1);
    }
    
    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectModeloParte != null) {
            service.create(selectModeloParte);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectModeloParte != null) {
            service.edit(selectModeloParte);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectModeloParte != null) {
            service.remove(selectModeloParte);
            init();
        }
    }

    public List<ModeloParte> getDataSource() {
        return dataSource;
    }

    public ModeloParte getSelectModeloParte() {
        if(selectModeloParte == null){
            selectModeloParte = new ModeloParte();
        }
        return selectModeloParte;
    }

    public void setSelectModeloParte(ModeloParte selectModeloParte) {
        this.selectModeloParte = selectModeloParte;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }
    
    
}
