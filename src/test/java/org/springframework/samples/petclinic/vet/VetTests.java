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
	void hasSpecialtyReturnsTrueForCaseInsensitiveMatch() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.hasSpecialty("surgery")).isTrue();
		assertThat(vet.hasSpecialty("Surgery")).isTrue();
	}

	@Test
	void hasSpecialtyReturnsTrueForTrimmedMatch() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.hasSpecialty(" surgery ")).isTrue();
		assertThat(vet.hasSpecialty(" Surgery")).isTrue();
	}

	@Test
	void hasSpecialtyReturnsFalseWhenNoSpecialtyMatches() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.hasSpecialty("dentistry")).isFalse();
		assertThat(vet.hasSpecialty("sur gery")).isFalse();
	}

	@Test
	void hasSpecialtyReturnsFalseWhenVetHasNoSpecialties() {
		Vet vet = new Vet();

		assertThat(vet.hasSpecialty("surgery")).isFalse();
	}

	@Test
	void hasSpecialtyReturnsFalseForNullOrBlankInput() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.hasSpecialty(null)).isFalse();
		assertThat(vet.hasSpecialty(" ")).isFalse();
		assertThat(vet.hasSpecialty("\t")).isFalse();
	}

	@Test
	void hasSpecialtyDoesNotModifySpecialties() {
		Vet vet = new Vet();
		Specialty surgery = specialty("surgery");
		Specialty radiology = specialty("radiology");
		vet.addSpecialty(surgery);
		vet.addSpecialty(radiology);

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).extracting(Specialty::getName).containsExactly("radiology", "surgery");

		assertThat(vet.hasSpecialty("surgery")).isTrue();
		assertThat(vet.hasSpecialty("dentistry")).isFalse();
		assertThat(vet.hasSpecialty(" ")).isFalse();

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).containsExactly(radiology, surgery);
	}

	@Test
	void specialtyHelpersRemainUnchanged() {
		Vet vet = new Vet();
		Specialty surgery = specialty("surgery");
		Specialty radiology = specialty("radiology");

		vet.addSpecialty(surgery);
		vet.addSpecialty(radiology);

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).containsExactly(radiology, surgery);
	}

	@Test
	void findSpecialtyReturnsMatchingSpecialtyForCaseInsensitiveName() {
		Vet vet = new Vet();
		Specialty surgery = specialty("surgery");
		vet.addSpecialty(surgery);

		assertThat(vet.findSpecialty("surgery")).contains(surgery);
		assertThat(vet.findSpecialty("Surgery")).contains(surgery);
	}

	@Test
	void findSpecialtyReturnsMatchingSpecialtyForTrimmedName() {
		Vet vet = new Vet();
		Specialty surgery = specialty("surgery");
		vet.addSpecialty(surgery);

		assertThat(vet.findSpecialty(" surgery ")).contains(surgery);
		assertThat(vet.findSpecialty(" Surgery")).contains(surgery);
	}

	@Test
	void findSpecialtyReturnsEmptyWhenNoSpecialtyMatches() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.findSpecialty("dentistry")).isEmpty();
		assertThat(vet.findSpecialty("sur gery")).isEmpty();
	}

	@Test
	void findSpecialtyReturnsEmptyWhenVetHasNoSpecialties() {
		Vet vet = new Vet();

		assertThat(vet.findSpecialty("surgery")).isEmpty();
	}

	@Test
	void findSpecialtyReturnsEmptyForNullOrBlankInput() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.findSpecialty(null)).isEmpty();
		assertThat(vet.findSpecialty(" ")).isEmpty();
		assertThat(vet.findSpecialty("\t")).isEmpty();
	}

	@Test
	void findSpecialtyDoesNotModifySpecialties() {
		Vet vet = new Vet();
		Specialty surgery = specialty("surgery");
		Specialty radiology = specialty("radiology");
		vet.addSpecialty(surgery);
		vet.addSpecialty(radiology);

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).containsExactly(radiology, surgery);

		assertThat(vet.findSpecialty("surgery")).contains(surgery);
		assertThat(vet.findSpecialty("dentistry")).isEmpty();
		assertThat(vet.findSpecialty(" ")).isEmpty();

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).containsExactly(radiology, surgery);
	}

	@Test
	void findSpecialtyPresenceMatchesHasSpecialty() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.findSpecialty("surgery").isPresent()).isEqualTo(vet.hasSpecialty("surgery"));
		assertThat(vet.findSpecialty("Surgery").isPresent()).isEqualTo(vet.hasSpecialty("Surgery"));
		assertThat(vet.findSpecialty(" surgery ").isPresent()).isEqualTo(vet.hasSpecialty(" surgery "));
		assertThat(vet.findSpecialty("dentistry").isPresent()).isEqualTo(vet.hasSpecialty("dentistry"));
		assertThat(vet.findSpecialty(null).isPresent()).isEqualTo(vet.hasSpecialty(null));
		assertThat(vet.findSpecialty(" ").isPresent()).isEqualTo(vet.hasSpecialty(" "));
	}

	private Specialty specialty(String name) {
		Specialty specialty = new Specialty();
		specialty.setName(name);
		return specialty;
	}

}
