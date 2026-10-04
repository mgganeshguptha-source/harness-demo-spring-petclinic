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
public class VetTests {

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
	void shouldReturnTrueWhenSpecialtyMatchesIgnoringCase() {
		Vet vet = vetWithSpecialties("surgery");

		assertThat(vet.hasSpecialty("surgery")).isTrue();
		assertThat(vet.hasSpecialty("Surgery")).isTrue();
	}

	@Test
	void shouldReturnTrueWhenNameMatchesAfterTrimmingOuterWhitespace() {
		Vet vet = vetWithSpecialties("surgery");

		assertThat(vet.hasSpecialty(" surgery ")).isTrue();
		assertThat(vet.hasSpecialty(" Surgery")).isTrue();
	}

	@Test
	void shouldReturnFalseWhenNoSpecialtyMatches() {
		Vet vet = vetWithSpecialties("surgery");

		assertThat(vet.hasSpecialty("dentistry")).isFalse();
	}

	@Test
	void shouldReturnFalseWhenVetHasNoSpecialties() {
		Vet vet = new Vet();

		assertThat(vet.hasSpecialty("surgery")).isFalse();
	}

	@Test
	void shouldReturnFalseForNullOrBlankNames() {
		Vet vet = vetWithSpecialties("surgery");

		assertThat(vet.hasSpecialty(null)).isFalse();
		assertThat(vet.hasSpecialty(" ")).isFalse();
		assertThat(vet.hasSpecialty("\t \n")).isFalse();
	}

	@Test
	void shouldNotMatchWhenWhitespaceDiffersInsideName() {
		Vet vet = vetWithSpecialties("surgery");

		assertThat(vet.hasSpecialty("sur gery")).isFalse();
	}

	@Test
	void shouldNotModifySpecialtiesWhenCheckingByName() {
		Vet vet = vetWithSpecialties("radiology", "surgery");

		List<Specialty> specialtiesBeforeCheck = vet.getSpecialties();

		assertThat(vet.hasSpecialty("Surgery")).isTrue();
		assertThat(vet.hasSpecialty("dentistry")).isFalse();

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).extracting(Specialty::getName)
			.containsExactlyElementsOf(specialtiesBeforeCheck.stream().map(Specialty::getName).toList());
	}

	@Test
	void shouldPreserveExistingSpecialtyManagementBehavior() {
		Vet vet = new Vet();

		vet.addSpecialty(specialty("radiology"));
		vet.addSpecialty(specialty("surgery"));

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).extracting(Specialty::getName).containsExactly("radiology", "surgery");
	}

	private Vet vetWithSpecialties(String... specialtyNames) {
		Vet vet = new Vet();
		for (String specialtyName : specialtyNames) {
			vet.addSpecialty(specialty(specialtyName));
		}
		return vet;
	}

	private Specialty specialty(String name) {
		Specialty specialty = new Specialty();
		specialty.setName(name);
		return specialty;
	}

}
