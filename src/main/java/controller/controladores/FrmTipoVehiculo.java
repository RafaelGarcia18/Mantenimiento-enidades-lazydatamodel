package controller.controladores;

import controller.acceso.TipoVehiculoFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.LazyDataModel;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoVehiculo;

/**
 *
 * @author rafael
 */
@Named(value = "frmTipoVehiculo")
@ViewScoped
public class FrmTipoVehiculo implements Serializable {

    private LazyDataModel<TipoVehiculo> lazymodel;
    private TipoVehiculo selectTipoVehiculo;

    @EJB
    private TipoVehiculoFacade service;

    @PostConstruct
    public void init() {
        final List<TipoVehiculo> dataSource = service.findAll();
        this.lazymodel = new AbstractLazyModel(dataSource) {
            @Override
            public Object getRowData(String rowKey) {
                for (TipoVehiculo car : dataSource) {
                    if (car.getIdTipoVehiculo().toString().equals(rowKey)) {
                        return car;
                    }
                }
                return null;
            }

            public Object getRowKey(TipoVehiculo car) {
                return car.getIdTipoVehiculo();
            }

        };
    }
    
    public void crear(){
        service.create(selectTipoVehiculo);
    }

    public void onRowSelected() {
        System.out.println("Row selected");
    }

    public LazyDataModel<TipoVehiculo> getLazymodel() {
        return lazymodel;
    }

    public TipoVehiculo getSelectTipoVehiculo() {
        return selectTipoVehiculo;
    }

    public void setSelectTipoVehiculo(TipoVehiculo selectTipoVehiculo) {
        this.selectTipoVehiculo = selectTipoVehiculo;
    }

}
