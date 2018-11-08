/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.ModeloFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.Modelo;

/**
 *
 * @author rafael
 */
@Named(value = "frmModelo")
@ViewScoped
public class FrmModelo implements Serializable {

    @EJB
    private ModeloFacade service;
    private Modelo selectModelo;
    private EstadoCRUD estado;
    private LazyDataModel<Modelo> lazyModel;
    private List<Modelo> dataSource;

    @PostConstruct
    private void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<Modelo>() {
            @Override
            public Object getRowKey(Modelo object) {
                if (object != null) {
                    return object.getIdModelo();
                }
                return null;
            }

            @Override
            public Modelo getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Integer search = new Integer(rowKey);
                        for (Modelo tu : (List<Modelo>) getWrappedData()) {
                            if (tu.getIdModelo().compareTo(search) == 0) {
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
            public List<Modelo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        this.selectModelo = new Modelo();
        this.lazyModel.setRowIndex(-1);
    }

    public void onRowSelect(SelectEvent event) {
        selectModelo = (Modelo) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectModelo = new Modelo();
        this.lazyModel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectModelo != null) {
            service.create(selectModelo);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectModelo != null) {
            service.edit(selectModelo);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectModelo != null) {
            service.remove(selectModelo);
            init();
        }
    }

    public LazyDataModel<Modelo> getLazyModel() {
        return lazyModel;
    }

    public Modelo getSelectModelo() {
        if (selectModelo == null) {
            selectModelo = new Modelo();
        }
        return selectModelo;
    }

    public void setSelectModelo(Modelo selectModelo) {
        this.selectModelo = selectModelo;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

}
