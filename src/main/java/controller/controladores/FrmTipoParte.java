package controller.controladores;

import controller.acceso.TipoParteFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoParte;

/**
 *
 * @author rafael
 */
@Named(value = "frmTipoParte")
@ViewScoped
public class FrmTipoParte implements Serializable {

    @EJB
    private TipoParteFacade service;
    private LazyDataModel<TipoParte> lazymodel;
    private TipoParte selectTipoParte;
    private List<TipoParte> dataSource;
    private EstadoCRUD estado;

    @PostConstruct
    public void init() {
        this.estado = EstadoCRUD.NUEVO;
        this.lazymodel = new LazyDataModel<TipoParte>() {
            @Override
            public Object getRowKey(TipoParte object) {
                if (object != null) {
                    return object.getIdTipoParte();
                }
                return null;
            }

            @Override
            public TipoParte getRowData(String rowKey) {
                if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                    try {
                        Integer search = new Integer(rowKey);
                        for (TipoParte tu : (List<TipoParte>) getWrappedData()) {
                            if (tu.getIdTipoParte().compareTo(search) == 0) {
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
            public List<TipoParte> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
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
        this.selectTipoParte = new TipoParte();
        this.lazymodel.setRowIndex(-1);
    }

    public void onRowSelect(SelectEvent event) {
        selectTipoParte = (TipoParte) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.selectTipoParte = new TipoParte();
        this.lazymodel.setRowIndex(-1);
    }

    public void btnCancelarHandler(ActionEvent ae) {
        init();
        this.estado = EstadoCRUD.NUEVO;
    }

    public void btnAgregarHandler(ActionEvent ae) {
        if (selectTipoParte != null) {
            service.create(selectTipoParte);
            init();
        }
    }

    public void btnEditarHandler(ActionEvent ae) {
        if (selectTipoParte != null) {
            service.edit(selectTipoParte);
            init();
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        if (selectTipoParte != null) {
            service.remove(selectTipoParte);
            init();
        }
    }

    public LazyDataModel<TipoParte> getLazymodel() {
        return lazymodel;
    }

    public TipoParte getSelectTipoParte() {
        if (selectTipoParte == null) {
            selectTipoParte = new TipoParte();
        }
        return selectTipoParte;
    }

    public void setSelectTipoParte(TipoParte selectTipoParte) {
        this.selectTipoParte = selectTipoParte;
    }

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

}
