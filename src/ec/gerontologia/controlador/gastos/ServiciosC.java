package ec.gerontologia.controlador.gastos;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.GastosDetalle;

@SuppressWarnings("serial")
public class ServiciosC extends SelectorComposer<Component>{
	@Wire private Window winServicios;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtCantidad;
	@Wire private Textbox txtPrecio;
	@Wire private Button btnAceptar;
	@Wire private Button btnSalir;
	
	GastosCabeceraC gastosCabeceraC;
	GastosDetalle detalle;
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		gastosCabeceraC = (GastosCabeceraC)Executions.getCurrent().getArg().get("VentanaPadre");
		
		detalle = new GastosDetalle();
	}

	@Listen("onClick=#btnAceptar")
	public void aceptar() {
		Integer cantidad = Integer.parseInt(txtCantidad.getText());
		Double precio = Double.parseDouble(txtPrecio.getText());
		detalle.setEstado("A");
		detalle.setIdDetalle(null);
		detalle.setTotal(cantidad * precio);
		gastosCabeceraC.llenarDetalle(detalle);
		salir();
	}
	
	@Listen("onClick=#btnSalir")
	public void salir() {
		winServicios.detach();
	}
	public GastosDetalle getDetalle() {
		return detalle;
	}

	public void setDetalle(GastosDetalle detalle) {
		this.detalle = detalle;
	}
	
	
}
