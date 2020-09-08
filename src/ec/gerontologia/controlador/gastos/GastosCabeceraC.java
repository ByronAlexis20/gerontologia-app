package ec.gerontologia.controlador.gastos;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import ec.gerontologia.modelo.Actividad;
import ec.gerontologia.modelo.ActividadDAO;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.Gasto;
import ec.gerontologia.modelo.GastoDAO;
import ec.gerontologia.modelo.GastosDetalle;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;
import ec.gerontologia.util.Context;

@SuppressWarnings("serial")
public class GastosCabeceraC extends SelectorComposer<Component>{
	@Wire private Window winCabeceraFactura;
	@Wire private Textbox txtCodigo;
	@Wire private Datebox dtpFecha;
	@Wire private Combobox cboActividad;
	@Wire private Button btnAgregar;
	@Wire private Button btnQuitar;
	@Wire private Listbox lstDetalle;
	@Wire private Textbox txtTotal;
	private List<GastosDetalle> detalles;
	private GastosDetalle detalleSeleccionado;
	
	ActividadDAO actividadDAO = new ActividadDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	GastoDAO gastoDAO = new GastoDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	}

	@Listen("onClick=#btnAgregar")
	public void agregar(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/gastos/servicios.zul", winCabeceraFactura, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnQuitar")
	public void quitar(){
		if(detalleSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un detalle!");
			return;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/gastos/servicios.zul", winCabeceraFactura, params);
		ventanaCargar.doModal();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void llenarDetalle(GastosDetalle detalle) {
		List<GastosDetalle> lista = new ArrayList<GastosDetalle>();
		for(Listitem item : lstDetalle.getItems()) {
			lista.add((GastosDetalle) item.getValue());
		}
		lista.add(detalle);
		sumarTotal(lista);
		lstDetalle.setModel(new ListModelList(lista));
		detalleSeleccionado = null;
		
	}
	
	@Listen("onClick=#btnGrabar")
	public void grabar() {
		if(dtpFecha.getValue() == null) {
			Clients.showNotification("Debe seleccionar fecha");
			return;
		}
		if(lstDetalle.getItems().size() == 0) {
			Clients.showNotification("No hay detalle!");
			return;
		}
		if(cboActividad.getSelectedItem() == null) {
			Clients.showNotification("Debe seleccionar actividad");
			return;
		}
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
			public void onEvent(ClickEvent event) throws Exception {
				if(Messagebox.Button.YES.equals(event.getButton())) {
					Actividad actividad = (Actividad)cboActividad.getSelectedItem().getValue();
					Gasto gastoGrabar = new Gasto();
					gastoGrabar.setEstado("A");
					gastoGrabar.setFecha(dtpFecha.getValue());
					gastoGrabar.setIdActividad(actividad.getIdActividad());
					gastoGrabar.setActividad(actividad.getDescripcion());
					gastoGrabar.setIdUsuario(Context.getInstance().getUsuarioLogeado().getIdUsuario());
					gastoGrabar.setTotal(Float.parseFloat(txtTotal.getText()));
					
					List<GastosDetalle> detallesGrabar = new ArrayList<GastosDetalle>();
					for(Listitem item : lstDetalle.getItems()) {
						GastosDetalle detalle = (GastosDetalle) item.getValue();
						detalle.setGasto(gastoGrabar);
						detallesGrabar.add(detalle);
					}
					gastoGrabar.setGastosDetalles(detallesGrabar);
					gastoDAO.getEntityManager().getTransaction().begin();
					gastoDAO.getEntityManager().persist(gastoGrabar);
					gastoDAO.getEntityManager().getTransaction().commit();
					Clients.showNotification("Datos grabados!");
					nuevo();
				}
			}
		};
		Messagebox.show("Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
				Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
	}
	
	public List<Actividad> getActividades(){
		Integer idPeriodo = 0;
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() > 0) {
			List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
			if(periodos.size() > 0) {
				idPeriodo = periodos.get(0).getIdPeriodo();
			}
		}
		List<Actividad> lista = actividadDAO.getActividades("", idPeriodo);
		return lista;
	}
	
	@Listen("onClick=#btnNuevo")
	private void nuevo() {
		lstDetalle.getItems().clear();
		txtCodigo.setText("0");
		txtTotal.setText("0.0");
		dtpFecha.setValue(new Date());
	}
	private void sumarTotal(List<GastosDetalle> lista) {
		Double total = 0.0;
		for(GastosDetalle gasto : lista) {
			total = total + gasto.getTotal();
		}
		txtTotal.setText(String.valueOf(total));
	}
	public List<GastosDetalle> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<GastosDetalle> detalles) {
		this.detalles = detalles;
	}
	public GastosDetalle getDetalleSeleccionado() {
		return detalleSeleccionado;
	}
	public void setDetalleSeleccionado(GastosDetalle detalleSeleccionado) {
		this.detalleSeleccionado = detalleSeleccionado;
	}
}
