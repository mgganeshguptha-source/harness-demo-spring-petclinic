/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 */
class VetTests {

	@Test
	void serialization() {
		Vet vet = new Vet();
		vet.setFirstName("Zaphod");
		vet.setLastName("Beeblebrox");
		vet.setId(123);
		@SuppressWarnings("deprecation")
		Vet other = (Vet) SerializationUtils.deserialize(SerializationUtils.serialize(vet));
		assertThat(other.getFirstName()).isEqualTo(vet.getFirstName());
		assertThat(other.getLastName()).isEqualTo(vet.getLastName());
		assertThat(other.getId()).isEqualTo(vet.getId());
	}

	@Test
	void hasSpecialtyReturnsTrueForCaseInsensitiveAndTrimmedMatches() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.hasSpecialty("surgery")).isTrue();
		assertThat(vet.hasSpecialty("Surgery")).isTrue();
		assertThat(vet.hasSpecialty(" surgery ")).isTrue();
		assertThat(vet.hasSpecialty(" Surgery")).isTrue();
	}

	@Test
	void hasSpecialtyReturnsFalseForUnmatchedName() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.hasSpecialty("dentistry")).isFalse();
	}

	@Test
	void hasSpecialtyReturnsFalseWhenVetHasNoSpecialties() {
		Vet vet = new Vet();

		assertThat(vet.hasSpecialty("surgery")).isFalse();
	}

	@Test
	void hasSpecialtyReturnsFalseForNullInput() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.hasSpecialty(null)).isFalse();
	}

	@Test
	void hasSpecialtyReturnsFalseForBlankInputAfterTrimming() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.hasSpecialty(" ")).isFalse();
		assertThat(vet.hasSpecialty("\t \n")).isFalse();
	}

	@Test
	void hasSpecialtyKeepsInternalWhitespaceSignificant() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.hasSpecialty("sur gery")).isFalse();
	}

	@Test
	void hasSpecialtyDoesNotModifyStoredSpecialties() {
		Vet vet = new Vet();
		Specialty surgery = specialty("surgery");
		Specialty radiology = specialty("radiology");
		vet.addSpecialty(surgery);
		vet.addSpecialty(radiology);
		List<Specialty> specialtiesBeforeLookup = vet.getSpecialties();

		assertThat(vet.hasSpecialty(" Surgery ")).isTrue();
		assertThat(vet.hasSpecialty("dentistry")).isFalse();
		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).containsExactlyElementsOf(specialtiesBeforeLookup);
		assertThat(vet.getSpecialties()).extracting(Specialty::getName).containsExactly("radiology", "surgery");
	}

	@Test
	void addSpecialtyGetSpecialtiesAndGetNrOfSpecialtiesRemainUnchanged() {
		Vet vet = new Vet();
		Specialty surgery = specialty("surgery");
		Specialty radiology = specialty("radiology");

		vet.addSpecialty(surgery);
		vet.addSpecialty(radiology);

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).containsExactly(radiology, surgery);
		assertThat(vet.hasSpecialty("radiology")).isTrue();
	}

	private Specialty specialty(String name) {
		Specialty specialty = new Specialty();
		specialty.setName(name);
		return specialty;
	}

}
