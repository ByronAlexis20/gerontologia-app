package ec.gerontologia.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class PersonaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Persona> getPersonaByCedula(String cedula) {
		List<Persona> resultado = new ArrayList<Persona>(); 
		Query query = getEntityManager().createNamedQuery("Persona.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		resultado = (List<Persona>) query.getResultList();
		return resultado;
	}

}
