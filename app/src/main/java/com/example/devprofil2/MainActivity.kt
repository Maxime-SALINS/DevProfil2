package com.example.devprofil2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devprofil2.ui.theme.DevProfil2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevProfil2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfilScreen(
                        profil = sampleProfil(),
                        onContactClick = {},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Écran principal : Column qui tient dans l'écran, sans scroll global.
// Les projets sont dans une LazyColumn qui prend la place restante (weight).
// ---------------------------------------------------------------------------
@Composable
fun ProfilScreen(
    profil: ProfilUi,
    onContactClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfilHeader(
            nom = profil.nom,
            role = profil.role,
            disponible = profil.disponible
        )
        DescriptionCard(description = profil.description)
        SectionTitle(titre = "Compétences")
        CompetencesRow(competences = profil.competences)
        SectionTitle(titre = "Projets")
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = profil.projets,
                key = { projet -> projet.id }
            ) { projet ->
                ProjectCard(projet = projet)
            }
        }
        ContactZone(
            email = profil.email,
            localisation = profil.localisation,
            disponibilite = profil.disponibilite
        )
        Button(
            onClick = onContactClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = profil.texteBouton)
        }
    }
}

// ---------------------------------------------------------------------------
// En-tête : Surface + Row + Box (avatar + badge)
// ---------------------------------------------------------------------------
@Composable
fun ProfilHeader(
    nom: String,
    role: String,
    disponible: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarAvecBadge(
                nom = nom,
                disponible = disponible
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nom,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun AvatarAvecBadge(
    nom: String,
    disponible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(88.dp)) {
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
            contentDescription = "Avatar de $nom",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .align(Alignment.TopStart)
        )
        if (disponible) {
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Disponible",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(2.dp)
                        .size(24.dp)
                )
            }
        }
    }
}

@Composable
fun DescriptionCard(
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun SectionTitle(
    titre: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = titre,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

// ---------------------------------------------------------------------------
// Liste horizontale (LazyRow) de compétences
// ---------------------------------------------------------------------------
@Composable
fun CompetencesRow(
    competences: List<String>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(competences) { competence ->
            TechnologyChip(label = competence)
        }
    }
}

@Composable
fun TechnologyChip(
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

// ---------------------------------------------------------------------------
// Carte de projet (élément de la LazyColumn)
// ---------------------------------------------------------------------------
@Composable
fun ProjectCard(
    projet: ProjetUi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = projet.titre,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = projet.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Technologies",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = projet.technologies,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Zone de contact : champs en lecture seule
// ---------------------------------------------------------------------------
@Composable
fun ContactZone(
    email: String,
    localisation: String,
    disponibilite: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            ContactField(
                label = "E-mail",
                value = email,
                icon = Icons.Default.Email
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ContactInfo(
                    icon = Icons.Default.LocationOn,
                    texte = localisation,
                    modifier = Modifier.weight(1f)
                )
                ContactInfo(
                    icon = Icons.Default.CheckCircle,
                    texte = disponibilite,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}

@Composable
fun ContactInfo(
    icon: ImageVector,
    texte: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = texte,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp)
        )
    }
}

@Composable
fun ContactField(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        singleLine = true,
        label = { Text(text = label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
        },
        modifier = modifier.fillMaxWidth()
    )
}

// ---------------------------------------------------------------------------
// Données locales
// ---------------------------------------------------------------------------
data class ProjetUi(
    val id: Int,
    val titre: String,
    val description: String,
    val technologies: String
)

data class ProfilUi(
    val nom: String,
    val role: String,
    val description: String,
    val competences: List<String>,
    val projets: List<ProjetUi>,
    val email: String,
    val localisation: String,
    val disponibilite: String,
    val disponible: Boolean,
    val texteBouton: String
)

fun sampleProfil(): ProfilUi {
    return ProfilUi(
        nom = "Maxime Salins",
        role = "Étudiant développeur",
        description = "Étudiant en informatique, à l'aise en PHP, " +
                "qui découvre le développement Android avec Jetpack Compose.",
        competences = listOf("PHP", "Kotlin", "Jetpack Compose", "Python", "Git", "Docker"),
        projets = listOf(
            ProjetUi(
                id = 1,
                titre = "DevProfil",
                description = "Première fiche de profil développeur avec composables réutilisables.",
                technologies = "Kotlin · Jetpack Compose"
            ),
            ProjetUi(
                id = 2,
                titre = "Boutique de chocolat",
                description = "Site de boutique en PHP natif, conteneurisé avec Docker.",
                technologies = "PHP · Docker · GitLab"
            ),
            ProjetUi(
                id = 3,
                titre = "Classification WDBC",
                description = "Prédiction du cancer du sein avec un réseau de neurones en NumPy.",
                technologies = "Python · NumPy · Colab"
            ),
            ProjetUi(
                id = 4,
                titre = "Menu latéral Symfony",
                description = "Menu de navigation dynamique piloté par les routes Symfony.",
                technologies = "Symfony · Twig · Stimulus"
            )
        ),
        email = "maxime.salins@gmail.com",
        localisation = "France",
        disponibilite = "Disponible pour un stage",
        disponible = true,
        texteBouton = "Contacter"
    )
}

// ---------------------------------------------------------------------------
// Previews
// ---------------------------------------------------------------------------
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp", name = "Profil Android - clair")
@Composable
fun ProfilScreenPreview() {
    DevProfil2Theme(darkTheme = false) {
        ProfilScreen(
            profil = sampleProfil(),
            onContactClick = {}
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp", name = "Profil Android - sombre")
@Composable
fun ProfilScreenDarkPreview() {
    DevProfil2Theme(darkTheme = true) {
        ProfilScreen(
            profil = sampleProfil(),
            onContactClick = {}
        )
    }
}
