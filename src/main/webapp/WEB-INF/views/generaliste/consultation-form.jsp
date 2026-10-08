<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Consultation Médicale" />
</jsp:include>

<div class="space-y-6">
    <!-- Patient Dossier Header -->
    <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4 border-b border-slate-100 pb-4">
            <div>
                <div class="flex items-center space-x-3">
                    <h1 class="text-2xl font-bold text-slate-900">${patient.nomComplet}</h1>
                    <span class="px-3 py-1 rounded-full text-xs font-mono font-semibold bg-sky-100 text-sky-800 border border-sky-200">
                        ${patient.numSecu}
                    </span>
                    <span class="px-3 py-1 rounded-full text-xs font-semibold bg-slate-100 text-slate-700">
                        Mutuelle : ${patient.mutuelle}
                    </span>
                </div>
                <p class="text-xs text-slate-500 mt-1">
                    Né(e) le : ${patient.dateNaissance} &bull; Tél : ${patient.telephone} &bull; Adresse : ${patient.adresse}
                </p>
            </div>
            <a href="${pageContext.request.contextPath}/generaliste/file-attente" class="text-sm font-medium text-slate-500 hover:text-slate-800">
                <i class="fa-solid fa-arrow-left mr-1"></i> Retour à la file d'attente
            </a>
        </div>

        <!-- Medical background & Vital signs row -->
        <div class="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
            <!-- Medical data -->
            <div class="bg-slate-50 p-4 rounded-xl text-xs space-y-2">
                <h3 class="font-bold text-slate-700 uppercase tracking-wider flex items-center">
                    <i class="fa-solid fa-book-medical mr-1.5 text-sky-600"></i> Données Médicales du Patient
                </h3>
                <div><span class="font-semibold text-slate-700">Antécédents :</span> ${not empty patient.antecedents ? patient.antecedents : 'Aucun'}</div>
                <div><span class="font-semibold text-slate-700">Allergies :</span> <span class="text-red-600 font-semibold">${not empty patient.allergies ? patient.allergies : 'Aucune'}</span></div>
                <div><span class="font-semibold text-slate-700">Traitements en cours :</span> ${not empty patient.traitements ? patient.traitements : 'Aucun'}</div>
            </div>

            <!-- Vital signs -->
            <div class="bg-sky-50/60 border border-sky-100 p-4 rounded-xl text-xs space-y-2">
                <h3 class="font-bold text-sky-800 uppercase tracking-wider flex items-center">
                    <i class="fa-solid fa-heart-pulse mr-1.5 text-sky-600"></i> Signes Vitaux Relevés par l'Infirmier
                </h3>
                <c:choose>
                    <c:when test="${not empty signesVitaux}">
                        <div class="grid grid-cols-3 gap-2 pt-1">
                            <div><span class="text-slate-500 block">Tension :</span> <strong class="text-slate-900">${signesVitaux.tensionArterielle}</strong> mmHg</div>
                            <div><span class="text-slate-500 block">Fréq. Cardiaque :</span> <strong class="text-slate-900">${signesVitaux.frequenceCardiaque}</strong> bpm</div>
                            <div><span class="text-slate-500 block">Température :</span> <strong class="text-slate-900">${signesVitaux.temperature}</strong> °C</div>
                            <div><span class="text-slate-500 block">Fréq. Respiratoire :</span> <strong class="text-slate-900">${signesVitaux.frequenceRespiratoire}</strong> cpm</div>
                            <div><span class="text-slate-500 block">Poids :</span> <strong class="text-slate-900">${signesVitaux.poids != null ? signesVitaux.poids : '-'}</strong> kg</div>
                            <div><span class="text-slate-500 block">Taille :</span> <strong class="text-slate-900">${signesVitaux.taille != null ? signesVitaux.taille : '-'}</strong> cm</div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-slate-400 italic">Aucun signe vital enregistré à l'accueil.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Consultation Common Section: Motif, Observations, Actes Techniques -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div class="lg:col-span-2 space-y-6">

            <!-- Common observations card -->
            <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
                <h2 class="text-lg font-bold text-slate-900 border-b border-slate-100 pb-3 flex items-center">
                    <i class="fa-solid fa-stethoscope text-sky-600 mr-2"></i> Examen Clinique & Observations
                </h2>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Motif de consultation *</label>
                    <input type="text" id="sharedMotif" required placeholder="ex: Douleur thoracique aiguë, essoufflement..."
                           class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                </div>
                <div>
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Observations cliniques & Analyse des symptômes *</label>
                    <textarea id="sharedObservations" rows="3" required placeholder="Description de l'examen physique (toucher, écouter...) et des plaintes du patient..."
                              class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500"></textarea>
                </div>

                <!-- Actes techniques médicaux -->
                <div class="pt-2">
                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-2">Actes techniques médicaux complémentaires</label>
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-2 text-xs">
                        <c:forEach var="a" items="${actesTechniques}">
                            <label class="flex items-center space-x-2 p-2.5 rounded-xl border border-slate-200 hover:bg-slate-50 cursor-pointer transition">
                                <input type="checkbox" name="actesCheck" value="${a.id}" data-tarif="${a.tarif}" onchange="recalculateTotal()"
                                       class="rounded text-sky-600 focus:ring-sky-500 h-4 w-4" />
                                <span class="flex-1 font-medium text-slate-800">${a.nom}</span>
                                <span class="font-bold text-sky-700 font-mono">${a.tarif} DH</span>
                            </label>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <!-- Decision Tabs: Scenario A vs Scenario B -->
            <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
                <div class="flex border-b border-slate-200 bg-slate-50">
                    <button type="button" id="tabBtnDirecte" onclick="switchScenario('directe')"
                            class="flex-1 py-3.5 px-4 text-center font-bold text-sm border-b-2 border-sky-600 text-sky-600 bg-white transition flex items-center justify-center space-x-2">
                        <i class="fa-solid fa-clipboard-check"></i>
                        <span>Scénario A : Prise en charge directe</span>
                    </button>
                    <button type="button" id="tabBtnExpertise" onclick="switchScenario('expertise')"
                            class="flex-1 py-3.5 px-4 text-center font-bold text-sm border-b-2 border-transparent text-slate-500 hover:text-slate-800 transition flex items-center justify-center space-x-2">
                        <i class="fa-solid fa-user-doctor"></i>
                        <span>Scénario B : Besoin d'une télé-expertise</span>
                    </button>
                </div>

                <!-- Form A: Prise en charge directe -->
                <div id="sectionDirecte" class="p-6 space-y-4">
                    <div class="p-3 bg-emerald-50 border border-emerald-200 rounded-xl text-xs text-emerald-800">
                        <i class="fa-solid fa-circle-info mr-1"></i> Le médecin généraliste prend en charge la situation directement, établit son diagnostic et délivre l'ordonnance.
                    </div>
                    <form id="formDirecte" action="${pageContext.request.contextPath}/generaliste/cloturer-directe" method="POST" onsubmit="prepareSubmit(this)">
                        <input type="hidden" name="csrfToken" value="${csrfToken}" />
                        <input type="hidden" name="patientId" value="${patient.id}" />
                        <input type="hidden" name="fileId" value="${fileItem != null ? fileItem.id : ''}" />
                        <input type="hidden" name="motif" id="formDirecteMotif" />
                        <input type="hidden" name="observations" id="formDirecteObservations" />
                        <div id="formDirecteActes"></div>

                        <div class="space-y-4">
                            <div>
                                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Diagnostic établi *</label>
                                <input type="text" name="diagnostic" required placeholder="Maladie ou affection identifiée..."
                                       class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500" />
                            </div>
                            <div>
                                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Prescription / Traitement prescrit *</label>
                                <textarea name="prescription" rows="3" required placeholder="Exemple : Paracétamol 1g (3 fois/jour) — Sirop antitussif (2 cuillères/jour)..."
                                          class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500 font-mono text-xs"></textarea>
                            </div>
                            <div class="flex justify-end pt-2">
                                <button type="submit" class="px-6 py-2.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl text-sm font-bold shadow-md transition flex items-center space-x-2">
                                    <i class="fa-solid fa-check-double"></i>
                                    <span>Clôturer la consultation (Statut : TERMINEE)</span>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Form B: Télé-expertise -->
                <div id="sectionExpertise" class="p-6 space-y-4 hidden">
                    <div class="p-3 bg-purple-50 border border-purple-200 rounded-xl text-xs text-purple-800">
                        <i class="fa-solid fa-circle-info mr-1"></i> Sollicitation de l'avis d'un spécialiste. La consultation sera enregistrée avec le statut <strong>EN_ATTENTE_AVIS_SPECIALISTE</strong>.
                    </div>

                    <form id="formExpertise" action="${pageContext.request.contextPath}/generaliste/demander-expertise" method="POST" onsubmit="prepareSubmit(this)">
                        <input type="hidden" name="csrfToken" value="${csrfToken}" />
                        <input type="hidden" name="patientId" value="${patient.id}" />
                        <input type="hidden" name="fileId" value="${fileItem != null ? fileItem.id : ''}" />
                        <input type="hidden" name="motif" id="formExpertiseMotif" />
                        <input type="hidden" name="observations" id="formExpertiseObservations" />
                        <div id="formExpertiseActes"></div>

                        <div class="space-y-4">
                            <!-- Étape 2 : Spécialité -->
                            <div>
                                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Étape 2 : Sélection de la spécialité médicale requise *</label>
                                <select id="selectSpecialite" onchange="onSpecialiteChange(this.value)"
                                        class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500 font-medium">
                                    <option value="">-- Choisir une spécialité --</option>
                                    <c:forEach var="sp" items="${specialites}">
                                        <option value="${sp.name()}">${sp.libelle}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Étape 3 : Liste des spécialistes (triés par tarif via Stream API) -->
                            <div>
                                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">
                                    Étape 3 : Recherche des spécialistes (filtré par spécialité & trié par tarif) *
                                </label>
                                <select id="selectSpecialiste" name="specialisteId" required onchange="onSpecialisteChange(this)"
                                        class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500 font-medium">
                                    <option value="" data-tarif="0">-- Sélectionnez d'abord une spécialité --</option>
                                </select>
                            </div>

                            <!-- Étape 4 : Créneaux horaires de 30 min -->
                            <div>
                                <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">
                                    Étape 4 : Choix du créneau horaire disponible (créneaux de 30 min) *
                                </label>
                                <select id="selectCreneau" name="creneauId" required
                                        class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500 font-medium">
                                    <option value="">-- Sélectionnez un spécialiste pour charger ses créneaux --</option>
                                </select>
                            </div>

                            <!-- Étape 5 : Question & Priorité -->
                            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                                <div class="sm:col-span-2">
                                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Question posée au spécialiste & données cliniques *</label>
                                    <textarea name="question" required rows="3" placeholder="Interprétation demandée, orientation diagnostique ou ajustement thérapeutique..."
                                              class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500"></textarea>
                                </div>
                                <div>
                                    <label class="block text-xs font-semibold text-slate-700 uppercase mb-1">Niveau de priorité *</label>
                                    <select name="priorite" required class="w-full px-3 py-2 border border-slate-300 rounded-xl text-sm focus:ring-2 focus:ring-sky-500">
                                        <option value="NORMALE">NORMALE</option>
                                        <option value="URGENTE">URGENTE</option>
                                        <option value="NON_URGENTE">NON URGENTE</option>
                                    </select>
                                </div>
                            </div>

                            <div class="flex justify-end pt-2">
                                <button type="submit" class="px-6 py-2.5 bg-purple-600 hover:bg-purple-700 text-white rounded-xl text-sm font-bold shadow-md transition flex items-center space-x-2">
                                    <i class="fa-solid fa-paper-plane"></i>
                                    <span>Demander avis spécialiste (Statut : EN_ATTENTE_AVIS_SPECIALISTE)</span>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- US4 : Cost Calculation Panel -->
        <div class="lg:col-span-1">
            <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm sticky top-24 space-y-4">
                <h3 class="text-base font-bold text-slate-900 border-b border-slate-100 pb-3 flex items-center">
                    <i class="fa-solid fa-file-invoice-dollar text-emerald-600 mr-2"></i> US4 : Calcul du Coût Total
                </h3>
                <p class="text-xs text-slate-400">
                    Calcul automatisé avec Java Streams & Lambdas <code class="bg-slate-100 px-1 rounded">map().sum()</code>.
                </p>

                <div class="space-y-3 text-sm pt-2">
                    <div class="flex justify-between items-center text-slate-600">
                        <span>Consultation généraliste :</span>
                        <span class="font-bold font-mono text-slate-900">150.00 DH</span>
                    </div>

                    <div class="flex justify-between items-center text-slate-600">
                        <span>Expertise spécialiste :</span>
                        <span id="displayTarifExpertise" class="font-bold font-mono text-slate-900">0.00 DH</span>
                    </div>

                    <div class="flex justify-between items-center text-slate-600">
                        <span>Actes techniques :</span>
                        <span id="displayTarifActes" class="font-bold font-mono text-slate-900">0.00 DH</span>
                    </div>

                    <div class="border-t border-slate-200 pt-3 flex justify-between items-center text-base">
                        <span class="font-bold text-slate-900">Coût Total :</span>
                        <span id="displayCoutTotal" class="font-extrabold font-mono text-xl text-emerald-600">150.00 DH</span>
                    </div>
                </div>

                <div class="p-3 bg-slate-50 rounded-xl text-xs text-slate-500 border border-slate-100">
                    <i class="fa-solid fa-shield mr-1"></i> Tarifs conventionnés affichés en Dirhams marocains (DH).
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    let currentScenario = 'directe';
    let currentExpertiseTarif = 0.0;

    function switchScenario(sc) {
        currentScenario = sc;
        const btnD = document.getElementById('tabBtnDirecte');
        const btnE = document.getElementById('tabBtnExpertise');
        const secD = document.getElementById('sectionDirecte');
        const secE = document.getElementById('sectionExpertise');

        if (sc === 'directe') {
            btnD.className = "flex-1 py-3.5 px-4 text-center font-bold text-sm border-b-2 border-sky-600 text-sky-600 bg-white transition flex items-center justify-center space-x-2";
            btnE.className = "flex-1 py-3.5 px-4 text-center font-bold text-sm border-b-2 border-transparent text-slate-500 hover:text-slate-800 transition flex items-center justify-center space-x-2";
            secD.classList.remove('hidden');
            secE.classList.add('hidden');
            currentExpertiseTarif = 0.0;
        } else {
            btnE.className = "flex-1 py-3.5 px-4 text-center font-bold text-sm border-b-2 border-purple-600 text-purple-600 bg-white transition flex items-center justify-center space-x-2";
            btnD.className = "flex-1 py-3.5 px-4 text-center font-bold text-sm border-b-2 border-transparent text-slate-500 hover:text-slate-800 transition flex items-center justify-center space-x-2";
            secE.classList.remove('hidden');
            secD.classList.add('hidden');
            const selSpec = document.getElementById('selectSpecialiste');
            const selectedOpt = selSpec.options[selSpec.selectedIndex];
            currentExpertiseTarif = selectedOpt ? parseFloat(selectedOpt.getAttribute('data-tarif') || '0') : 0;
        }
        recalculateTotal();
    }

    function recalculateTotal() {
        const checkboxes = document.querySelectorAll('input[name="actesCheck"]:checked');
        let totalActes = 0.0;
        checkboxes.forEach(cb => {
            totalActes += parseFloat(cb.getAttribute('data-tarif') || '0');
        });

        const base = 150.0;
        const exp = (currentScenario === 'expertise') ? currentExpertiseTarif : 0.0;
        const total = base + exp + totalActes;

        document.getElementById('displayTarifActes').innerText = totalActes.toFixed(2) + " DH";
        document.getElementById('displayTarifExpertise').innerText = exp.toFixed(2) + " DH";
        document.getElementById('displayCoutTotal').innerText = total.toFixed(2) + " DH";
    }

    function onSpecialiteChange(spec) {
        const selSpec = document.getElementById('selectSpecialiste');
        const selCreneau = document.getElementById('selectCreneau');
        selSpec.innerHTML = '<option value="">Chargement des spécialistes...</option>';
        selCreneau.innerHTML = '<option value="">-- Sélectionnez un spécialiste --</option>';

        if (!spec) {
            selSpec.innerHTML = '<option value="" data-tarif="0">-- Sélectionnez une spécialité --</option>';
            currentExpertiseTarif = 0;
            recalculateTotal();
            return;
        }

        fetch('${pageContext.request.contextPath}/api/specialistes?specialite=' + encodeURIComponent(spec))
            .then(res => res.json())
            .then(data => {
                selSpec.innerHTML = '<option value="" data-tarif="0">-- Choisir un spécialiste --</option>';
                data.forEach(s => {
                    const opt = document.createElement('option');
                    opt.value = s.id;
                    opt.setAttribute('data-tarif', s.tarif);
                    opt.textContent = s.nomComplet + " (" + s.tarif + " DH)";
                    selSpec.appendChild(opt);
                });
            })
            .catch(err => {
                selSpec.innerHTML = '<option value="" data-tarif="0">Erreur de chargement</option>';
            });
    }

    function onSpecialisteChange(selectElem) {
        const doctorId = selectElem.value;
        const selCreneau = document.getElementById('selectCreneau');
        const selectedOpt = selectElem.options[selectElem.selectedIndex];
        currentExpertiseTarif = selectedOpt ? parseFloat(selectedOpt.getAttribute('data-tarif') || '0') : 0;
        recalculateTotal();

        if (!doctorId) {
            selCreneau.innerHTML = '<option value="">-- Sélectionnez un spécialiste --</option>';
            return;
        }

        selCreneau.innerHTML = '<option value="">Chargement des créneaux disponibles...</option>';
        fetch('${pageContext.request.contextPath}/api/specialistes?doctorId=' + encodeURIComponent(doctorId))
            .then(res => res.json())
            .then(slots => {
                selCreneau.innerHTML = '';
                if (slots.length === 0) {
                    selCreneau.innerHTML = '<option value="">Aucun créneau disponible pour ce spécialiste</option>';
                    return;
                }
                slots.forEach(c => {
                    const opt = document.createElement('option');
                    opt.value = c.id;
                    opt.textContent = c.date + " : " + c.plageHoraire + " (" + c.statut + ")";
                    selCreneau.appendChild(opt);
                });
            })
            .catch(err => {
                selCreneau.innerHTML = '<option value="">Erreur chargement créneaux</option>';
            });
    }

    function prepareSubmit(form) {
        const motif = document.getElementById('sharedMotif').value;
        const obs = document.getElementById('sharedObservations').value;

        if (form.id === 'formDirecte') {
            document.getElementById('formDirecteMotif').value = motif;
            document.getElementById('formDirecteObservations').value = obs;
            const container = document.getElementById('formDirecteActes');
            container.innerHTML = '';
            document.querySelectorAll('input[name="actesCheck"]:checked').forEach(cb => {
                const inp = document.createElement('input');
                inp.type = 'hidden';
                inp.name = 'actes';
                inp.value = cb.value;
                container.appendChild(inp);
            });
        } else {
            document.getElementById('formExpertiseMotif').value = motif;
            document.getElementById('formExpertiseObservations').value = obs;
            const container = document.getElementById('formExpertiseActes');
            container.innerHTML = '';
            document.querySelectorAll('input[name="actesCheck"]:checked').forEach(cb => {
                const inp = document.createElement('input');
                inp.type = 'hidden';
                inp.name = 'actes';
                inp.value = cb.value;
                container.appendChild(inp);
            });
        }
    }
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
