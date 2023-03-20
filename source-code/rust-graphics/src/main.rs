use std::{process::exit, thread, time};

const WIDTH: usize = 900;
const HEIGHT: usize = 900;

struct Image {
    width: usize,
    height: usize,
    buffer: Vec<u32>,
}

impl Image {
    /// Load a PNG-file as an `Image`.
    fn load(filename: &str) -> Image {
        let image =
            lodepng::decode32_file(&filename).expect(&format!("Couldn't load {}", &filename));
        Image {
            width: image.width,
            height: image.height,
            buffer: image
                .buffer
                .iter()
                .map(|color| {
                    ((color.a as u32) << 24)
                        | ((color.r as u32) << 16)
                        | ((color.g as u32) << 8)
                        | ((color.b as u32) << 0)
                })
                .collect(),
        }
    }

    fn rotate(self: &Self, buffer: &mut Vec<u32>, rotation: f64, superimposed: bool) {
        // Calculate "margin" so that it rotates around the center of the image instead of the top-left corner
        let y_margin = (HEIGHT / 2) as i32;
        let x_margin = (WIDTH / 2) as i32;

		let rotation_sin = rotation.sin();
		let rotation_cos = rotation.cos();

        for y in 0..HEIGHT {
            let y_offset = y * WIDTH;
            let screen_y = y as i32 - y_margin;

            // Calculate y_sin and y_cos separately to improve performance
            let y_sin = screen_y as f64 * rotation_sin;
            let y_cos = screen_y as f64 * rotation_cos;

            for x in 0..WIDTH {
                let screen_x = x as i32 - x_margin;

                // Calculate the x_sin and x_cos
                let x_sin = screen_x as f64 * rotation_sin;
                let x_cos = screen_x as f64 * rotation_cos;

                // Calculate the rotated position for a pixel, using rem_euclid to prevent pixels outside the window.
                let rotated_x = (x_cos + y_sin).rem_euclid(self.width as f64) as u32;
                let rotated_y = (x_sin - y_cos).rem_euclid(self.height as f64) as u32;

                // Calculate buffer offset normally, and image offset with rotated position values
                let buffer_offset = y_offset + x;
                let img_offset = rotated_y * self.width as u32 + rotated_x;

                if superimposed {
                    // Convert buffer and image pixel u32s to list of color values.
                    let mut buf_pixel = buffer[buffer_offset as usize].to_be_bytes();
                    let img_pixel = self.buffer[img_offset as usize].to_be_bytes();

                    // For each buffer and image color, divide value by two and add them together
                    for i in 0..4 {
                        buf_pixel[i] = buf_pixel[i] / 2 + img_pixel[i] / 2
                    }

                    // Convert list of pixel colors back to a u32
                    buffer[buffer_offset as usize] = ((buf_pixel[0] as u32) << 24)
                        | ((buf_pixel[1] as u32) << 16)
                        | ((buf_pixel[2] as u32) << 8)
                        | ((buf_pixel[3] as u32) << 0);


					// buffer[buffer_offset as usize] =
					// 	(buffer[buffer_offset as usize] >> 1 & DIVISION_MASK) +
					// 	(self.buffer[img_offset as usize] >> 1 & DIVISION_MASK);

                } else {
                    buffer[buffer_offset as usize] = self.buffer[img_offset as usize];
                }
            }
        }
    }
}

fn main() {
    // Load the two images
    let image1 = Image::load("mainframe1.png");
    let image2 = Image::load("mainframe2.png");

    // Create a window
    let mut window = minifb::Window::new(
        "Rotating images",
        WIDTH as usize,
        HEIGHT as usize,
        minifb::WindowOptions {
            resize: false,
            topmost: true,
            scale: minifb::Scale::FitScreen,
            scale_mode: minifb::ScaleMode::AspectRatioStretch,
            ..minifb::WindowOptions::default()
        },
    )
    .expect("Unable to create window");

    // The output buffer that we'll render the output image to
    let mut buffer = vec![0u32; (WIDTH * HEIGHT) as usize];

    // The current rotation in radians (0..2*Pi)
    let mut rotation: f64 = 0.0;

    // Whether we are just drawing a single image (0), rotating a single image (1),
    // or rotating two superimposed images in different directions (2).
    let mut mode = 0;

    // Variables to keep track of frames per second
    let mut fps_time = std::time::Instant::now();
    let mut fps_count = 0;

    // The main loop
    while window.is_open() && !window.is_key_down(minifb::Key::Escape) {
        // Rotate a bit
        rotation += 0.01;

        // Change the mode
        if window.is_key_pressed(minifb::Key::Space, minifb::KeyRepeat::No) {
            mode = (mode + 1) % 3;
        }

        // Draw!
        if mode == 0 {
            for y in 0..image1.height {
                // Calculate image and buffer offset
                let y_img_offset = y * image1.width;
                let y_buffer_offset = y * WIDTH;

                for x in 0..image1.width {
                    // Add x to buffer offsets
                    let buffer_offset = y_buffer_offset + x;
                    let img_offset = y_img_offset + x;

                    // Set the image pixel to the correct buffer pixel
                    buffer[buffer_offset] = image1.buffer[img_offset];
                }
            }
        } else if mode == 1 {
            image1.rotate(&mut buffer, rotation, false)
        } else {
            image1.rotate(&mut buffer, rotation, false);
            image2.rotate(&mut buffer, -rotation, true);
        }

        // Show the buffer in the window
        window
            .update_with_buffer(&buffer, WIDTH as usize, HEIGHT as usize)
            .unwrap();

        // Display the FPS rate 3 times per second
        let now = std::time::Instant::now();
        let duration = (now - fps_time).as_secs_f32();
        fps_count += 1;
        if duration >= 0.33 {
            println!("{} fps", (fps_count as f32 / duration).round() as u32);
            fps_time = now;
            fps_count = 0;
        }
    }
}
