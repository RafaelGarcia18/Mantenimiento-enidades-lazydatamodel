/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.TipoUsuarioFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoUsuario;

/**
 *
 * @author rafael
 */
@Named(value = "frmTipoUsuario")
@ViewScoped
public class FrmTipoUsuario implements Serializable {

    @EJB
    private TipoUsuarioFacade service;
    private LazyDataModel<TipoUsuario> lazymodel;
    private TipoUsuario selectTipoUsuario;
    private EstadoCRUD estado;
    private List<TipoUsuario> dataSource;

    @PostConstruct
    public void init() {

        this.estado = EstadoCRUD.NUEVO;
        this.lazymodel = new LazyDataModel<TipoUsuario>() {
            @Override
            public Object getRowKey(TipoUsuario object) {
                if (object != null) {
                    return object.getIdTipoUsuario();
                }
                return null;
            }

            @Override
            public TipoUsuario getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Integer search = new Integer(rowKey);
                        for (TipoUsuario tu : (List<TipoUsuario>) getWrappedData()) {
                            if (tu.getIdTipoUsuario().compareTo(search) == 0) {
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
            public List<TipoUsuario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        this.selectTipoUsuario = new TipoUsuario();
        this.lazymodel.setRowIndex(-1);
    }

    public void onRowSelect(SelectEvent event) {
        selectTipoUsuario = (TipoUsuario) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectTipoUsuario = new TipoUsuario();
        this.lazymodel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectTipoUsuario != null) {
            service.create(selectTipoUsuario);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectTipoUsuario != null) {
            service.edit(selectTipoUsuario);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectTipoUsuario != null) {
            service.remove(selectTipoUsuario);
            init();
        }
    }

    public LazyDataModel<TipoUsuario> getLazymodel() {
        return lazymodel;
    }

    public TipoUsuario getSelectTipoUsuario() {
        if (selectTipoUsuario == null) {
            selectTipoUsuario = new TipoUsuario();
        }
        return selectTipoUsuario;
    }

    public void setSelectTipoUsuario(TipoUsuario selectTipoUsuario) {
        this.selectTipoUsuario = selectTipoUsuario;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

}
