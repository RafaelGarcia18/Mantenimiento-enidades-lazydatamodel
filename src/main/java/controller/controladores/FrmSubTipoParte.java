/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.controladores;

import controller.acceso.SubTipoParteFacade;
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
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.SubTipoParte;

/**
 *
 * @author rafael
 */
@Named(value = "frmSubTipoParte")
@ViewScoped
public class FrmSubTipoParte implements Serializable {

    @EJB
    private SubTipoParteFacade service;
    private SubTipoParte selectSubTParte;
    private EstadoCRUD estado;
    private LazyDataModel<SubTipoParte> lazyModel;
    private List<SubTipoParte> dataSource;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModel = new LazyDataModel<SubTipoParte>() {
            @Override
            public SubTipoParte getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Integer search = new Integer(rowKey);
                        for (SubTipoParte tu : (List<SubTipoParte>) getWrappedData()) {
                            if (tu.getIdSubTipoParte().compareTo(search) == 0) {
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
            public Object getRowKey(SubTipoParte object) {
                if (object != null) {
                    return object.getIdSubTipoParte();
                }
                return null;
            }

            @Override
            public int getRowIndex() {
                return super.getRowIndex();
            }

            @Override
            public List<SubTipoParte> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        };
        this.selectSubTParte = new SubTipoParte();
        this.lazyModel.setRowIndex(-1);
    }
    
    public void onRowSelect(SelectEvent event) {
        selectSubTParte = (SubTipoParte) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectSubTParte = new SubTipoParte();
        this.lazyModel.setRowIndex(-1);
    }
    
    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectSubTParte != null) {
            service.create(selectSubTParte);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectSubTParte != null) {
            service.edit(selectSubTParte);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectSubTParte != null) {
            service.remove(selectSubTParte);
            init();
        }
    }

    public List<SubTipoParte> getDataSource() {
        return dataSource;
    }

    public SubTipoParte getSelectSubTParte() {
        if(selectSubTParte == null){
        selectSubTParte = new SubTipoParte();
    }
        return selectSubTParte;
    }

    public void setSelectSubTParte(SubTipoParte selectSubTParte) {
        this.selectSubTParte = selectSubTParte;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }
    

}
