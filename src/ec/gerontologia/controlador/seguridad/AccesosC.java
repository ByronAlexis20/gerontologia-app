package ec.gerontologia.controlador.seguridad;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Menu;
import ec.gerontologia.modelo.MenuDAO;
import ec.gerontologia.modelo.Perfil;
import ec.gerontologia.modelo.PerfilDAO;
import ec.gerontologia.modelo.Permiso;
import ec.gerontologia.modelo.PermisoDAO;

public class AccesosC {
	@Wire private Window winAccesos;

	
	// Instancia el objeto para acceso a datos.
	private PermisoDAO permisoDAO = new PermisoDAO();
	private PerfilDAO perfilDAO = new PerfilDAO();
	private MenuDAO menuDAO = new MenuDAO();

	private Permiso permiso = new Permiso();
	private List<Permiso> permisosAsignadas = new ArrayList<>();
	private List<Permiso> permisosEliminados = new ArrayList<>();
	private List<Menu> opcionesDisponible = new ArrayList<>();

	//para mi cuadritos de acceso
	private Perfil perfilSeleccionado;
	private Menu opcionSeleccionadoAgregar;
	private Permiso opcionSeleccionadoQuitar;
	
	
	/**
	 * Metodo que emula al metodo doAfterComposer de MVC
	 */
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		llenarOpciones();
	}	
	
	private void llenarOpciones() {
		try {
			opcionesDisponible.clear();
			opcionesDisponible = menuDAO.getOpcionesActivos();
			for(Menu mnu : opcionesDisponible) {
				if(mnu.getIdMenuPadre() != 0) 
					mnu.setTitulo(menuDAO.getMenuPadre(mnu.getIdMenuPadre()) + "/" + mnu.getDescripcion());
				else 
					opcionesDisponible.remove(mnu);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Command
	@NotifyChange({"permisosAsignadas", "opcionesDisponible"})
	public void cargarOpciones() {
		//cargar las opciones disponibles
		permisosEliminados.clear();
		permisosAsignadas.clear();
		List<Permiso> listaPermisoAsignados = permisoDAO.getPermisosAsignados(perfilSeleccionado.getIdPerfil());
		for(Permiso permiso : listaPermisoAsignados){
			permiso.getMenu().setTitulo(menuDAO.getMenuPadre(permiso.getMenu().getIdMenuPadre()) + "/" + permiso.getMenu().getDescripcion());
			permisosAsignadas.add(permiso);
		}
		
		//cargar las opciones asignadas
		List<Menu> listaOpciones = new ArrayList<>();
		opcionesDisponible.clear();
		listaOpciones = menuDAO.getOpcionesActivos();
		List<Integer> idOpcionesAsignadas = new ArrayList<Integer>();
		List<Permiso> listaPermiso = permisoDAO.getPermisosAsignados(perfilSeleccionado.getIdPerfil());
		
		for(Permiso permiso : listaPermiso) idOpcionesAsignadas.add(permiso.getMenu().getIdMenu());//para obtener todos los id que han sido asignados
		System.out.println(opcionesDisponible.size());
		for(Menu mnu : listaOpciones) {
			mnu.setTitulo(menuDAO.getMenuPadre(mnu.getIdMenuPadre()) + "/" + mnu.getDescripcion());
			if(!idOpcionesAsignadas.contains(mnu.getIdMenu()))
				opcionesDisponible.add(mnu);
		}
	}
	
	@Command
	@NotifyChange({"opcionSeleccionadoAgregar", "opcionesDisponible", "permisosAsignadas"})
	public void agregar() {
		if(opcionSeleccionadoAgregar != null) {
			System.out.println(opcionSeleccionadoAgregar.getTitulo());
			Permiso permisoAgregar = new Permiso();
			permisoAgregar.setEstado("A");
			permisoAgregar.setIdPermiso(null);
			permisoAgregar.setMenu(opcionSeleccionadoAgregar);
			permisoAgregar.setPerfil(perfilSeleccionado);
			
			permisosAsignadas.add(permisoAgregar);
			opcionesDisponible.remove(opcionSeleccionadoAgregar);	
			opcionSeleccionadoAgregar = null;
		}
	}

	@Command
	@NotifyChange({"opcionesDisponible", "permisosAsignadas"})
	public void agregartodo () {
		if (!opcionesDisponible.isEmpty()) {
			for (Menu op : opcionesDisponible) {
				Permiso permisoAgregar = new Permiso();
				permisoAgregar.setEstado("A");
				permisoAgregar.setIdPermiso(null);
				permisoAgregar.setMenu(op);
				permisoAgregar.setPerfil(perfilSeleccionado);
				permisosAsignadas.add(permisoAgregar);				
			}
			opcionesDisponible = new ArrayList<>();
		}	
	}

	@Command
	@NotifyChange({"opcionSeleccionadoQuitar", "opcionesDisponible", "permisosAsignadas"})
	public void quitar () {
		if(opcionSeleccionadoQuitar != null) {
			permisosEliminados.add(opcionSeleccionadoQuitar);
			System.out.println(opcionSeleccionadoQuitar.getMenu().getTitulo());
			opcionesDisponible.add(opcionSeleccionadoQuitar.getMenu());
			permisosAsignadas.remove(opcionSeleccionadoQuitar);
			opcionSeleccionadoQuitar = null;
		}
	}

	@Command
	@NotifyChange({"opcionesDisponible", "permisosAsignadas"})
	public void quitartodo () {		
		if (!permisosAsignadas.isEmpty()) {
			for (Permiso permiso : permisosAsignadas) {
				opcionesDisponible.add(permiso.getMenu());	
				permisosEliminados.add(permiso);
			}
			permisosAsignadas = new ArrayList<>();
		}	
	}	
	
	/** Graba la informacion.*/
	@Command
	public void grabar(){
		try {			
			if (perfilSeleccionado == null) {
				Clients.showNotification("Seleccion el perfil");
				return;
			}		
			if (!permisosAsignadas.isEmpty()) {
				for (Permiso objeto : permisosAsignadas) {
					Permiso permiso = permisoDAO.getPermisoPerfilMenu(objeto.getPerfil().getIdPerfil(), objeto.getMenu().getIdMenu());
					if(permiso != null) {//diferente de null manda a modificar
						permisoDAO.getEntityManager().getTransaction().begin();
						permisoDAO.getEntityManager().merge(objeto);
						permisoDAO.getEntityManager().getTransaction().commit();
					}else {//es un nuevo permiso
						permisoDAO.getEntityManager().getTransaction().begin();
						permisoDAO.getEntityManager().persist(objeto);
						permisoDAO.getEntityManager().getTransaction().commit();
					}
				}
			}	
			
			if (!opcionesDisponible.isEmpty()) {
				for (Menu objeto : opcionesDisponible) {
					Permiso permiso = permisoDAO.getPermisoPerfilMenu(perfilSeleccionado.getIdPerfil(), objeto.getIdMenu());
					if (permiso != null) {
						permiso.setEstado("1");
						permisoDAO.getEntityManager().getTransaction().begin();
						permisoDAO.getEntityManager().merge(permiso);
						permisoDAO.getEntityManager().getTransaction().commit();
					}
				}	
			}

			Clients.showNotification("Proceso Ejecutado con exito.");
			// Actualiza la lista
			//BindUtils.postGlobalCommand(null, null, "UsuarioPerfilLista.buscar", null);

		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			permisoDAO.getEntityManager().getTransaction().rollback();
		}		
	}
	public List<Perfil> getPerfiles() {
		return perfilDAO.getPerfiles();
	}
	public Permiso getPermiso() {
		return permiso;
	}
	public void setPermiso(Permiso permiso) {
		this.permiso = permiso;
	}
	public Perfil getPerfilSeleccionado() {
		return perfilSeleccionado;
	}
	public void setPerfilSeleccionado(Perfil perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}
	public List<Permiso> getPermisosAsignadas() {
		return permisosAsignadas;
	}
	public void setPermisosAsignadas(List<Permiso> permisosAsignadas) {
		this.permisosAsignadas = permisosAsignadas;
	}
	public List<Menu> getOpcionesDisponible() {
		return opcionesDisponible;
	}
	public void setOpcionesDisponible(List<Menu> opcionesDisponible) {
		this.opcionesDisponible = opcionesDisponible;
	}
	public Menu getOpcionSeleccionadoAgregar() {
		return opcionSeleccionadoAgregar;
	}
	public void setOpcionSeleccionadoAgregar(Menu opcionSeleccionadoAgregar) {
		this.opcionSeleccionadoAgregar = opcionSeleccionadoAgregar;
	}
	public Permiso getOpcionSeleccionadoQuitar() {
		return opcionSeleccionadoQuitar;
	}
	public void setOpcionSeleccionadoQuitar(Permiso opcionSeleccionadoQuitar) {
		this.opcionSeleccionadoQuitar = opcionSeleccionadoQuitar;
	}

	public List<Permiso> getPermisosEliminados() {
		return permisosEliminados;
	}

	public void setPermisosEliminados(List<Permiso> permisosEliminados) {
		this.permisosEliminados = permisosEliminados;
	}
	
}
