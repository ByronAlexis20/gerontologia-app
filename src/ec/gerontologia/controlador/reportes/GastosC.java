package ec.gerontologia.controlador.reportes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Actividad;
import ec.gerontologia.modelo.ActividadDAO;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.Gasto;
import ec.gerontologia.modelo.GastoDAO;
import ec.gerontologia.modelo.Instruccion;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;
import ec.gerontologia.util.PrintReport;

@SuppressWarnings("serial")
public class GastosC extends SelectorComposer<Component>{
	@Wire private Window winGastosLista;
	@Wire private Button btnBuscar;
	@Wire private Listbox lstGastos;
	
	private List<Gasto> gastos;
	private Gasto gastoSeleccionado;
	GastoDAO gastoDAO = new GastoDAO();
	ActividadDAO actividadDAO = new ActividadDAO();
	
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		buscar();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar")
	public void buscar(){
		System.out.println("entra buscar");
		
		if (gastos != null) {
			gastos = null; 
		}
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() == 0) {
			return;
		}
		Integer idPeriodo = 0;
		List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
		if(periodos.size() == 0) {
			return;
		}
		idPeriodo = periodos.get(0).getIdPeriodo();
		List<Actividad> listaActividad = actividadDAO.getActividades("", idPeriodo);
		List<Gasto> lista = new ArrayList<Gasto>();
		for(Actividad act : listaActividad) {
			List<Gasto> listaGasto = gastoDAO.getGasto(act.getIdActividad());
			for(Gasto g : listaGasto) {
				lista.add(g);
			}
		}
		lstGastos.setModel(new ListModelList(lista));
		gastoSeleccionado = null;
	}
	@Listen("onClick=#btnImprimir")
	public void imprimir(){
		if(gastoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un gasto");
			return;
		}
		PrintReport reporte = new PrintReport();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ID_GASTO", gastoSeleccionado.getIdGasto());
		param.put("NO_COMPROBANTE", String.valueOf(gastoSeleccionado.getIdGasto()));
		param.put("ACTIVIDAD", gastoSeleccionado.getActividad());
		param.put("FECHA", String.valueOf(gastoSeleccionado.getFecha()));
		param.put("TOTAL", String.valueOf(gastoSeleccionado.getTotal()));
		reporte.crearReporte("/recursos/reportes/gastos.jasper", gastoDAO, param);
	}

	public List<Gasto> getGastos() {
		return gastos;
	}

	public void setGastos(List<Gasto> gastos) {
		this.gastos = gastos;
	}

	public Gasto getGastoSeleccionado() {
		return gastoSeleccionado;
	}

	public void setGastoSeleccionado(Gasto gastoSeleccionado) {
		this.gastoSeleccionado = gastoSeleccionado;
	}
	

}
