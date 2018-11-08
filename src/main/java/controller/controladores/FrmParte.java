/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.ParteFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.Parte;

/**
 *
 * @author rafael
 */
@Named(value = "frmParte")
@ViewScoped
public class FrmParte implements Serializable {

    @EJB
    private ParteFacade service;
    private Parte selectParte;
    private EstadoCRUD estado;
    private LazyDataModel<Parte> lazyModel;
    private List<Parte> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<Parte>() {
            @Override
            public Parte getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Integer search = new Integer(rowKey);
                        for (Parte tu : (List<Parte>) getWrappedData()) {
                            if (tu.getIdParte().compareTo(search) == 0) {
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
            public Object getRowKey(Parte object) {
                if (object != null) {
                    return object.getIdParte();
                }
                return null;
            }

            @Override
            public List<Parte> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
                return super.getRowIndex(); //To change body of generated methods, choose Tools | Templates.
            }
        };
        this.selectParte = new Parte();
        this.lazyModel.setRowIndex(-1);
    }

    public void onRowSelect(SelectEvent event) {
        selectParte = (Parte) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectParte = new Parte();
        this.lazyModel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectParte != null) {
            service.create(selectParte);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectParte != null) {
            service.edit(selectParte);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectParte != null) {
            service.remove(selectParte);
            init();
        }
    }

    public List<Parte> getDataSource() {
        return dataSource;
    }

    public Parte getSelectParte() {
        if (selectParte == null) {
            selectParte = new Parte();
        }
        return selectParte;
    }

    public void setSelectParte(Parte selectParte) {
        this.selectParte = selectParte;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

}
