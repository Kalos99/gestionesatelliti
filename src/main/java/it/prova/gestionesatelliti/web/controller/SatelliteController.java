package it.prova.gestionesatelliti.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {
	
	@Autowired
	private SatelliteService satelliteService;
	
	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}

	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}
	
	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {
		List<Satellite> results = satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}
	
	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attr", new Satellite());
		return "satellite/insert";
	}
	
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) {
		
		if(satellite.getDataDiLancio() != null && satellite.getDataDiRientro() != null && (satellite.getDataDiRientro().equals(satellite.getDataDiLancio()) || satellite.getDataDiRientro().before(satellite.getDataDiLancio()))) {
			result.rejectValue("dataDiRientro", "dataDiLancio.dataDiRientro.rangeInvalid");
		}

		if (result.hasErrors())
			return "satellite/insert";

		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}
	
	@GetMapping("/preparalancio/{idSatellite}")
	public String preparalancio(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("prepara_lancio_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/preparalancio";
	}
	
	@PostMapping("/lancia")
	public String lancia(@RequestParam(required = true) Long idSatellite, RedirectAttributes redirectAttrs) {
		satelliteService.lancia(idSatellite);
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
	
	@GetMapping("/prepararientro/{idSatellite}")
	public String prepararientro(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("prepara_rientro_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/prepararientro";
	}
	
	@PostMapping("/rientra")
	public String rientra(@RequestParam(required = true) Long idSatellite, RedirectAttributes redirectAttrs) {
		satelliteService.rientra(idSatellite);
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}
}